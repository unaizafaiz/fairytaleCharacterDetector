# Fairytale Character Detector - Abstract

Detection of characters in folktales is a complex task, as these stories involve animals, objects etc., as characters. Characters are often identified with noun phrases like ‘the old woman’ in addition to proper nouns. Such character names are not detected by a Named Entity Recognizer. This project aims at detection of all characters in folktales using three methods. First, using Stanford CoreNLP parser for named entity recognition. Second, fine tuning the detection of all proper nouns using POS tagger and filtering using WordNet. Finally, characters identified by common nouns and/or noun phrases are extracted by identifying subject-verb and verb-object phrases. These phrases are filtered using two features of WordNet, namely sentence frame and derivationally related form to detect the verbs that are often associated with human activity. The characters detected from all methods are then combined to produce the final output and the results are evaluated.


