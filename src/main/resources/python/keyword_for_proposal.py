from konlpy.tag import Mecab
import json
import sys

class KeywordForProposal:
  dic_dir = None;
  def __init__(self, dic_dir = "/home/leems83/services/mecab-0.996-ko-0.9.2/lib/mecab/dic/mecab-ko-dic"):
    self.dic_dir = dic_dir
  def getNounList(self, str):
    mecab = Mecab(self.dic_dir)
    return mecab.nouns(str)
    
keywordForProposal = KeywordForProposal()
result = keywordForProposal.getNounList(sys.argv[1])
print(json.dumps(result))