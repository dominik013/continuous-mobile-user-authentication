from sklearn.model_selection import TimeSeriesSplit

from conversion import *


def get_sequences_from_df(df_paths, normalized_features):
    """
    Converts multiple pandas data frame to a list of sequences with the following pre-processing applied:
        + Min-Max normalized all columns passed with :attr:`normalized_features`
        + Converting the data frame to a list of numpy arrays
        + Returning only sequences that are not empty and have length > 1
    :param df_paths: The paths of all pandas data frames to convert
    :param normalized_features: The features which should be normalized
    :return: A list of numpy arrays containing stroke information
    """
    stroke_sequences = []
    for path in df_paths:
        strokes = get_data_frame(path)
        strokes = normalize_df_columns_min_max(strokes, normalized_features)
        strokes = convert_to_numpy_sequences(strokes, normalized_features)
        strokes = get_sequences_where_length_bigger_than(strokes, 1)

        stroke_sequences = stroke_sequences + strokes

    return stroke_sequences


def get_sequences_from_df_length(paths, normalized_features, length):
    """
    Converts multiple pandas data frame to a list of sequences with the following pre-processing applied:
        + Min-Max normalized all columns passed with :attr:`normalized_features`
        + Converting the data frame to a list of numpy arrays
        + Returning only sequences which have exactly :attr:`length` rows
    :param paths: The paths of all pandas data frames to convert
    :param normalized_features: The features which should be normalized
    :param length: How many rows a sequence must have exactly
    :return: A list of numpy arrays containing stroke information
    """
    stroke_sequences = []
    for path in paths:
        strokes = get_data_frame(path)
        strokes = normalize_df_columns_min_max(strokes, normalized_features)
        strokes = convert_to_numpy_sequences(strokes, normalized_features)
        strokes = get_sequences_with_length(strokes, length)

        stroke_sequences = stroke_sequences + strokes

    return stroke_sequences


def normalize_timestamps(strokes_df):
    """
    Normalizes the passed timestamp by subtracting the minimum value to get a range from 0 to (duration of stroke)
    :param strokes_df: A pandas data frame containing strokes
    :return: A pandas data frame with normalized timestamps
    """
    ids = strokes_df.abs_mt_tracking_id.unique()
    result = []
    for tracking_id in ids:
        index = strokes_df.loc[strokes_df['abs_mt_tracking_id'] == tracking_id].idxmin()[0]
        min_time = strokes_df.at[index, 'timestamp']
        s = strokes_df.loc[strokes_df['abs_mt_tracking_id'] == tracking_id]
        s['timestamp'] = s['timestamp'] - min_time
        result.append(s)

    return pd.concat(result)


def get_ltr_hmm_parameters(states):
    """
    Returns the initialized transition matrix and start probabilities for a left-to-right HMM
    :param states: The number of states the HMM is using
    :return: float array (states, states) : transition matrix
    :return: float array (states) : start probabilities
    """
    transition_matrix = np.zeros((states, states))

    for i in range(states):
        if i == states - 1:
            transition_matrix[i, i] = 1.0
        else:
            transition_matrix[i, i] = transition_matrix[i, i + 1] = 0.5

    # Always start in first state
    start_probabilities = np.zeros(states)
    start_probabilities[0] = 1.0

    return transition_matrix, start_probabilities


def train_model_k_fold(model, sequences, n_splits):
    """
    Trains the passed model with the passed data
    with n_splits-fold cross validation and returns
    the average score of the model.
    :param model: The Hidden Markov Model to use the score method on
    :param sequences: The sequences from which the k-fold validation is made
    :param n_splits: The number of splits done in cross-validation
    :return: float : The cross-validated score for the passed model and dataset
    """

    kf = TimeSeriesSplit(n_splits)
    scores = []

    for train_index, test_index in kf.split(sequences):
        train = [sequences[i] for i in train_index]
        test = [sequences[i] for i in test_index]

        model.fit(np.concatenate(train), get_lengths_of_sequences(train))
        score = get_normalized_score(model, test)
        scores.append(score)
    return sum(scores) / len(scores)


def get_normalized_score(model, sequences):
    """
    Returns the summed up score for a list of sequences
    :param model: The Hidden Markov Model to use the score method on
    :param sequences: The sequences from which the score is calculated from
    :return: float : The summed up score for a list of sequences
    """
    return sum(get_individual_normalized_scores(model, sequences))


def get_individual_normalized_scores(model, sequences):
    """
    Returns the individual, length-normalized scores for each sequence as a list
    :param model: The Hidden Markov Model to use the score method on
    :param sequences: The sequences from which the scores are calculated from
    :return: List<Float> : A list of individual scores for the passed sequences
    """
    scores = []
    for seq in sequences:
        scores.append(model.score(seq) / len(seq))
    return scores
