from selenium import webdriver
from flask import Flask, request
import threading
import json
import subprocess
import re

app = Flask(__name__)

def runPyFile(scnrio_num, dtm, test_datas):
  global g_testResult
  if dtm not in g_testResult.keys():
    g_testResult[dtm] = {}
  if scnrio_num not in g_testResult[dtm].keys():
    g_testResult[dtm][scnrio_num] = {}
  for test_data in test_datas:
    f = open('test.py', 'w')
    f.write(test_data['src_cd'])
    f.close()
    g_testResult[dtm][scnrio_num][test_data['case_num']] = {"proc_st": 1, "test_rslt": []}
    try :
      result = subprocess.check_output('python test.py', shell=True)
    except CalledProcessError as ex:
      g_testResult[dtm][scnrio_num][test_data['case_num']]["test_rslt"].append({"test_rslt_cd": 3, "log": ex})
      g_testResult[dtm][scnrio_num][test_data['case_num']]["proc_st"] = 2
      continue
    g_testResult[dtm][scnrio_num][test_data['case_num']]["proc_st"] = 2
    result_split = result.split()
    for each_result in result_split:
      g_testResult[dtm][scnrio_num][test_data['case_num']]["test_rslt"].append({"test_rslt_cd": 1, "log": each_result})

@app.route('/test', methods=['POST'])
def requestTest():
  t = threading.Thread(target=runPyFile, args=(request.form['scnrio_num'], request.form['dtm'], json.loads(request.form['test_data'])))
  t.start()
  return ''

@app.route('/getTestResult', methods=['POST'])
def requestGetTestResult():
  global g_testResult
  if int(request.form['dtm']) not in g_testResult.keys():
    return '{"test_st_cd": 1}'
  if int(request.form['scnrio_num']) not in g_testResult[int(request.form['dtm'])]:
    return '{"test_st_cd": 1}'
  if int(request.form['case_num']) not in g_testResult[int(request.form['dtm'])][int(request.form['scnrio_num'])]:
    return '{"test_st_cd": 1}'
  if len(g_testResult[int(request.form['dtm'])][int(request.form['scnrio_num'])][int(request.form['case_num'])]["test_rslt"]) < int(request.form['test_step_num']):
    if g_testResult[int(request.form['dtm'])][int(request.form['scnrio_num'])][int(request.form['case_num'])]["proc_st"] == 2:
      return '{"test_st_cd": 2, "test_rslt_cd": 4}'
    else :
      return '{"test_st_cd": 1}'
  ret_data = g_testResult[int(request.form['dtm'])][int(request.form['scnrio_num'])][int(request.form['case_num'])]["test_rslt"][int(request.form['test_step_num'])]
  ret_data["test_st_cd"] = 2
  return json.dumps(ret_data)

@app.after_request
def add_header(response):
  pattern = re.compile('://kokodog(dev){0,1}\.fun25\.co\.kr')
  if pattern.search(request.headers['Origin']) != None :
    response.headers['Access-Control-Allow-Origin'] = request.headers['Origin']
    response.headers['Access-Control-Allow-Methods'] = 'POST'
    response.headers['Access-Control-Max-Age'] = '3600'
    response.headers['Access-Control-Allow-Headers'] = 'Origin,Accept,X-Requested-With,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization'
  return response
  
def fileCheck():
  return

if __name__ == '__main__':
  t = threading.Thread(target=fileCheck, args=())
  t.start()
  g_testResult = {}
  app.run(host='0.0.0.0', port=30710)
