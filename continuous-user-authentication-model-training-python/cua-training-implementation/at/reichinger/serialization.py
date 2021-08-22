import json

import numpy as np


class NumpyEncoder(json.JSONEncoder):
    """
    Encoder for JSON serializing multidimensional numpy arrays
    """

    def default(self, obj):
        if isinstance(obj, np.ndarray):
            return obj.tolist()
        return json.JSONEncoder.default(self, obj)


def write_model_to_json(model, file):
    """
    Writes the provided GMM-HMM to JSON
    :param model: The trained GMM - Hidden Markov Model object
    :param file: The path where to save the *.json file to
    :return: -
    """
    if not file.endswith('.json'):
        raise ValueError(f'File {file} does not have a JSON ending')
    with open(file, 'w') as f:
        f.write(_dump_model(model))


def _dump_model(model):
    """
    Returns the JSON representation of the model
    :param model: The trained GMM - Hidden Markov Model object
    :return: A JSON string representing the passed model
    """
    return json.dumps(
        {
            'n_components': model.n_components,
            'n_features': model.n_features,
            'n_mixtures': model.n_mix,
            'covariance_type': model.covariance_type,
            'start_probabilities': model.startprob_,
            'transition_matrix': model.transmat_,
            'weights': model.weights_,
            'means': model.means_,
            'covariance_matrix': model.covars_
        },
        cls=NumpyEncoder)
