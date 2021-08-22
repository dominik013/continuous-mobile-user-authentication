import os.path
import sys

from hmmlearn import hmm

from data_preparation import *
from serialization import write_model_to_json

db_path = sys.argv[1]
np.random.seed(42)

if not os.path.isfile(db_path):
    print(f'Database file [{db_path}] does not exist!')
    exit(1)

"""
Reading the data and splitting into train and test sets
"""
print(f'Reading database file [{db_path}] ...')
used_features = ['x_position', 'y_position']
X = get_sequences_from_df([db_path], used_features)
X_train, X_validate = generate_train_test_split(X, 0.85)
print(f'Train set size: {len(X_train)} Validation set size: {len(X_validate)}')

"""
Training of the model with different number of states and mixtures
"""
max_states = 3
max_mixtures = 5

best_score = float('-inf')
best_model = []
for states in range(2, max_states):
    for mixtures in range(2, max_mixtures):
        print(f'Training model with {states} states and {mixtures} mixtures.', end=' ')

        transmat, startprob = get_ltr_hmm_parameters(states)

        model = hmm.GMMHMM(
            n_components=states,
            n_mix=mixtures
        )

        model.startprob_ = startprob
        model.transmat_ = transmat

        score = train_model_k_fold(model, X_train, 10)
        print(f'Score {score}')

        if score > best_score:
            best_score = score
            best_model = [states, mixtures, score]

states, mixtures, score = best_model

print(f'Best model has {states} states, {mixtures} mixtures with a score of {score}')

"""
Re-train model with best number of states and mixtures with all data
"""
model = hmm.GMMHMM(
    n_components=states,
    n_mix=mixtures
)

transmat, startprob = get_ltr_hmm_parameters(states)

model.startprob_ = startprob
model.transmat_ = transmat

model.fit(np.concatenate(X_train), get_lengths_of_sequences(X_train))

"""
Printing achieved scores of the model
"""
test_dataset_list = ['../../data/test1/cua-db.db', '../../data/test3/cua-db.db', '../../data/test4/cua-db.db']
Y = []
for dataset in test_dataset_list:
    print(dataset)
    sequences = get_sequences_from_df([dataset], used_features)
    Y.append(sequences)

score_positive = get_individual_normalized_scores(model, X_validate)
scores_negative = []
for data in Y:
    s = get_individual_normalized_scores(model, data[0:len(X_validate)])
    scores_negative.append(s)
    print(f'Score negative: {sum(s)}')

"""
Writing the resulting model to a JSON file for further usage
"""
write_model_to_json(model, '../../data/final_model.json')
