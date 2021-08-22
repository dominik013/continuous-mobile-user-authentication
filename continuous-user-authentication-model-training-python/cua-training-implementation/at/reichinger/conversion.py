import sqlite3
import numpy as np
import pandas as pd
from sklearn.preprocessing import StandardScaler


def get_data_frame(path):
    """
    Returns a pandas data frame from the specified path
    :param path: A path to a CUA database which was recorded using the CUARecorder Android application
    :return: A pandas dataframe from the
    """
    conn = sqlite3.connect(path)
    query = "SELECT * FROM touch_input"

    df = pd.read_sql_query(query, conn)
    df = df[['abs_mt_tracking_id', 'x_position', 'y_position', 'accelerometer_x', 'accelerometer_y', 'accelerometer_z',
             'timestamp']]
    df = df.replace(-1, pd.NaT)
    df = df.dropna()
    df = df.drop_duplicates(subset=['x_position', 'y_position'])

    return df


def get_lengths_of_sequences(sequences):
    """
    Returns the lengths of sequences passed to the method
    :param sequences: list of numpy arrays : A list of sequences
    :return: The lengths of all sequences passed to the method
    """
    lengths = []
    for seq in sequences:
        lengths.append(len(seq))

    return lengths


def data_length_iteration(strokes, min_length=1, columns=('x_position', 'y_position')):
    """
    Takes a pandas data frame and converts it to a single numpy array plus the corresponding lengths of each stroke
    :param strokes: Pandas data frame containing stroke information
    :param min_length: The minimum length for a single stroke, otherwise it will not be included
    :param columns: The required columns to include in the output
    :return: numpy array (nSamples, columns) : data
    :return: array (size tracking_id_max - tracking_id_min) : lengths of the single strokes
    """
    tracking_id_min = strokes['abs_mt_tracking_id'].min()
    tracking_id_max = strokes['abs_mt_tracking_id'].max()

    data = _numpy_array_for_tracking_id(strokes, tracking_id_min, columns)
    lengths = [len(data)]

    for tracking_id in range(tracking_id_min, tracking_id_max):
        stroke = strokes.loc[strokes['abs_mt_tracking_id'] == tracking_id]
        pos = stroke[columns].to_numpy()
        pos_length = len(pos)

        if pos_length >= min_length:
            data = np.concatenate([data, pos])
            lengths.append(len(pos))

    return data, lengths


def _numpy_array_for_tracking_id(strokes, tracking_id, columns):
    """
    Returns a numpy array from a data frame for the specified tracking_id
    :param strokes: Pandas data frame containing stroke information
    :param tracking_id: The id of the stroke
    :param columns: The required columns to include in the output
    :return: array (count(tracking_id), columns) : A numpy array for the specified tracking_id
    """
    stroke = strokes.loc[strokes['abs_mt_tracking_id'] == tracking_id]
    pos = stroke[columns].to_numpy()
    return pos


def convert_to_numpy_sequences(strokes, columns=('x_position', 'y_position')):
    """
    Converts a given pandas data frame to a list of numpy sequences, each sequence containing information of a single
    stroke
    :param strokes: Pandas data frame containing stroke information
    :param columns: The required columns to include in the output
    :return: list of array ((count(tracking_id), columns)) : A list of numpy arrays including all strokes
    """
    tracking_id_min = strokes['abs_mt_tracking_id'].min()
    tracking_id_max = strokes['abs_mt_tracking_id'].max()

    stroke_list = []
    for tracking_id in range(tracking_id_min, tracking_id_max):
        stroke = strokes.loc[strokes['abs_mt_tracking_id'] == tracking_id]
        pos = stroke[columns].to_numpy()
        stroke_list.append(pos)

    return stroke_list


"""
Data preparation
"""


def normalize_df_columns_min_max(strokes, columns=('x_position', 'y_position')):
    """
    Normalizes the specified columns in a data frame with the min-max normalization technique
    :param strokes: Pandas data frame containing stroke information
    :param columns: The required columns to include in the output
    :return: A pandas data frame with normalized columns
    """
    result = strokes.copy()
    for column in columns:
        min_value = strokes[column].min()
        max_value = strokes[column].max()
        if max_value > min_value:
            result[column] = (strokes[column] - min_value) / (max_value - min_value)

    return result


def standardize_df_columns(strokes, columns=('x_position', 'y_position')):
    """
    Standardizes the specified columns in a data frame using sklearn's StandardScaler
    :param strokes: Pandas data frame containing stroke information
    :param columns: The required columns to include in the output
    :return: A pandas data frame with standardized columns
    """
    result = strokes.copy()
    sc = StandardScaler()
    result[columns] = sc.fit_transform(result[columns])
    return result


"""
Splitting data to train/test/validate sets
"""


def generate_train_test_split(sequences, train_percentage=0.7):
    """
    Splits a list of sequences into a training and testing data set
    :param sequences: A list of sequences
    :param train_percentage: The percentage of sequences going into the train set
    :return: list (train_percentage) : A list of train sequences
    :return: list(1 - train_percentage) : A list of test sequences
    """
    train_length = int(len(sequences) * train_percentage)
    train = sequences[0:train_length]
    test = sequences[train_length:len(sequences)]
    return train, test


def generate_train_test_validate_split(sequences, train_percentage=0.7, test_percentage=0.2):
    """
    Splits a list of sequences into a training, testing and validation data set
    :param sequences: A list of sequences
    :param train_percentage: The percentage of sequences going into the train set
    :param test_percentage: The percentage of sequences going into the test set
    :return: list (train_percentage) : A list of train sequences
    :return: list(test_percentage) : A list of test sequences
    :return: list(1 - (train_percentage + test_percentage)) : A list of validation sequences
    """
    train_length = int(len(sequences) * train_percentage)
    test_length = int(len(sequences) * test_percentage)

    train = sequences[0:train_length]
    test = sequences[train_length:(train_length + test_length)]
    validate = sequences[(train_length + test_length):len(sequences)]
    return train, test, validate


"""
List sequence manipulation methods
"""


def get_sequences_with_length(sequences, length):
    """
    Returns only sequences with the specified length
    :param sequences: A list of sequences
    :param length: The length the sequences must match to be included in the output
    :return: list : Sequences with specified length
    """
    result = []
    for seq in sequences:
        if seq.shape[0] == length:
            result.append(seq)

    return result


def get_sequences_where_length_not(sequences, length):
    """
    Returns only sequences without the specified length
    :param sequences: A list of sequences
    :param length: The length the sequences must NOT match to be included in the output
    :return: list : Sequences without specified length
    """
    result = []
    for seq in sequences:
        if seq.shape[0] != length:
            result.append(seq)

    return result


def get_sequences_where_length_bigger_than(sequences, value):
    """
    Returns only sequences where length is bigger than
    :param sequences: A list of sequences
    :param value: The length the sequences must be bigger than this to be included in the output
    :return: list : Sequences with length bigger than :attr:`value`
    """
    result = []
    for seq in sequences:
        if seq.shape[0] > value:
            result.append(seq)

    return result


def get_sequences_where_length_smaller_than(sequences, value):
    """
    Returns only sequences where length is smaller than
    :param sequences: A list of sequences
    :param value: The length the sequences must be smaller than this to be included in the output
    :return: list : Sequences with length smaller than :attr:`value`
    """
    result = []
    for seq in sequences:
        if seq.shape[0] < value:
            result.append(seq)

    return result
