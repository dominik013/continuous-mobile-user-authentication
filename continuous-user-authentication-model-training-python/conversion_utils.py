import sqlite3
import numpy as np
import pandas as pd
from sklearn.preprocessing import StandardScaler
from typing import List

def getDataFrameFromPath(path):
    conn = sqlite3.connect(path)
    query = "SELECT * FROM touch_input"

    df = pd.read_sql_query(query, conn)
    df = df[['abs_mt_tracking_id', 'x_position', 'y_position', 'accelerometer_x', 'accelerometer_y', 'accelerometer_z', 'timestamp']]
    df = df.replace(-1, pd.NaT)
    df = df.dropna()
    df = df.drop_duplicates(subset=['x_position', 'y_position'])

    return df

def getLengthsOfSequences(sequences: List[int]) -> List[int]:
    lengths = []
    for seq in sequences:
        lengths.append(len(seq))

    return lengths

def convertToSingleNumpySequence(strokes, min_length = 1, columns = ['x_position', 'y_position']):
    tracking_id_min = strokes['abs_mt_tracking_id'].min()
    tracking_id_max = strokes['abs_mt_tracking_id'].max()

    X = _getNumpyArrayForTrackingId(strokes, tracking_id_min, columns)
    lengths = [len(X)]

    for tracking_id in range(tracking_id_min, tracking_id_max):
        stroke = strokes.loc[strokes['abs_mt_tracking_id'] == tracking_id]
        pos = stroke[columns].to_numpy()
        pos_length = len(pos)

        if pos_length >= min_length:
            X = np.concatenate([X, pos])
            lengths.append(len(pos))

    return X, lengths

def _getNumpyArrayForTrackingId(strokes, tracking_id, columns):
    stroke = strokes.loc[strokes['abs_mt_tracking_id'] == tracking_id]
    pos = stroke[columns].to_numpy()
    return pos

def convertToNumpySequences(strokes, columns = ['x_position', 'y_position']):
    tracking_id_min = strokes['abs_mt_tracking_id'].min()
    tracking_id_max = strokes['abs_mt_tracking_id'].max()

    stroke_list = []
    for tracking_id in range(tracking_id_min, tracking_id_max):
        stroke = strokes.loc[strokes['abs_mt_tracking_id'] == tracking_id]
        pos = stroke[columns].to_numpy()
        stroke_list.append(pos)

    return stroke_list



##
## Data preparation
##

def normalizeDataframeColumnsMinMax(strokes, columns = ['x_position', 'y_position']):
    result = strokes.copy()
    for column in columns:
        min_value = strokes[column].min()
        max_value = strokes[column].max()
        if max_value > min_value:
            result[column] = (strokes[column] - min_value) / (max_value - min_value)

    return result

def standardizeDataframeColumns(strokes, columns = ['x_position', 'y_position']):
    result = strokes.copy()
    scaler = StandardScaler() 
    result[columns] = scaler.fit_transform(result[columns])
    return result

##
## Splitting data to train/test/validate sets
##

def generateTrainTestSplit(sequences, train_percentage = 0.7):
    train_length = int(len(sequences) * train_percentage)
    train = sequences[0:train_length]
    test = sequences[train_length:len(sequences)]
    return train, test

def generateTrainTestValidateSplit(sequences, train_percentage = 0.7, test_percentage = 0.2):
    train_length = int(len(sequences) * train_percentage)
    test_length = int(len(sequences) * test_percentage)

    train = sequences[0:train_length]
    test = sequences[train_length:(train_length + test_length)]
    validate = sequences[(train_length + test_length):len(sequences)]
    return train, test, validate


##
## List sequence manipualtion methods
##
    
def getSequencesWithLength(sequences, length):
    result = []
    for seq in sequences:
        if seq.shape[0] == length:
            result.append(seq)
            
    return result

def getSequencesWhereLengthNot(sequences, length):
    result = []
    for seq in sequences:
        if seq.shape[0] != length:
            result.append(seq)
            
    return result

def getSequencesWhereLengthBiggerThan(sequences, value):
    result = []
    for seq in sequences:
        if seq.shape[0] > value:
            result.append(seq)
            
    return result

def getSequencesWhereLengthSmallerThan(sequences, value):
    result = []
    for seq in sequences:
        if seq.shape[0] < value:
            result.append(seq)
            
    return result