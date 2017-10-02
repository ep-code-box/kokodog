from konlpy.tag import Mecab
import json
import sys

class MorphemeAnalysis:
  dic_dir = None;
  def __init__(self, dic_dir = "/home/leems83/services/mecab-0.996-ko-0.9.2/lib/mecab/dic/mecab-ko-dic"):
    self.dic_dir = dic_dir
  def getMorphemeList(self, str):
    mecab = Mecab(self.dic_dir)
    return mecab.pos(str)
    
morphemeAnalysis = MorphemeAnalysis()
result = morphemeAnalysis.getMorphemeList(sys.argv[1])
result_json_type = []
for i in range(0, len(result)):
  result_json_type = result_json_type + [{"voca" : result[i][0], "type" : result[i][1]}]
print(json.dumps(result_json_type))