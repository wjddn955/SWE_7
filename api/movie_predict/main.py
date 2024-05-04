import os
import argparse
import pandas as pd
from model import load_classifier, preprocess_data, predict_with_classifier

parser = argparse.ArgumentParser(description='Process some factors')
parser.add_argument('--factors', required=True, help='director_name,duration,director_facebook_likes,actor_3_facebook_likes,actor_2_name,actor_1_facebook_likes,gross,genres,actor_1_name,num_voted_users,actor_3_name,facenumber_in_poster,plot_keywords,num_user_for_reviews,language,country,content_rating,budget,title_year,actor_2_facebook_likes,aspect_ratio,movie_facebook_likes')

args = parser.parse_args()
movie_factors = args.factors


classifier = load_classifier('./model.joblib')

df = pd.DataFrame(preprocess_data(movie_factors))

print(predict_with_classifier(classifier, df))