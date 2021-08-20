# Ensemble Classification Demo
Implemented `TextCNNs`,`BERT` as well as the *weighted vote* ensemble method。
- Run entry: [src/main/java/EnsembleDemo.java](https://github.com/cyk1337/BERT-Text-Classification-J/blob/18c6444447b3e3eac93d5def8de81392afcb5297/src/main/java/EnsembleDemo.java)

## Preparation
1. Firstly, you must have exported either `BERT`/`TextCNNs` models into `.pb` format. For BERT models, you can find the solution by checking https://github.com/cyk1337/BERT-Classification/blob/master/bert/run_classification.py.
2. Then, use the `.pb` model path to initialize the model class in Java, suppose you have generated `.pb` model correctly.
