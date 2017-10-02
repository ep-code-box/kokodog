import MeCab
import json
import sys

class MorphemeAnalysis:
  dic_dir = None;
  def __init__(self, dic_dir = "/home/leems83/services/mecab-0.996-ko-0.9.2/lib/mecab/dic/mecab-ko-dic"):
    self.dic_dir = dic_dir
  def getMorphemeList(self, str):
    mecab = MeCab.Tagger("-d " + self.dic_dir)
    return mecab.parse(str)
    
morphemeAnalysis = MorphemeAnalysis()
result = morphemeAnalysis.getMorphemeList(sys.argv[1]).split("\n")
result_json_type = []
for i in range(0, len(result)):
  type_temp = result[i].split('\t')
  if len(type_temp) >= 2 :
    type_temp_sub = type_temp[len(type_temp) - 1].split(',')
    result_json_type = result_json_type + [{"voca" : type_temp[0], "type" : type_temp_sub}]
print(json.dumps(result_json_type))