# Movie Prediction API

This API provides movie prediction functionality based on input movie factors.

## Features

### Movie Prediction
- **Description**: Predicts the success level of a movie based on provided factors.
- **REST API**:
  - Endpoint: `/features/movie_predict`
  - Method: POST
  - Request Body: 
    - `factor`: A comma-separated string containing movie factors
  - Response: Returns one of the following strings: `'FLOP'`, `'AVG'`, `'HIT'` indicating the predicted success level of the movie.

## Example Usage

### Movie Prediction API Examples

#### Example 1:
curl -X POST -F "factor=James Cameron,178,0,855,Joel David Moore,1000,760505847,Action|Adventure|Fantasy|Sci-Fi,CCH Pounder,886204,Wes Studi,0,avatar|future|marine|native|paraplegic,3054,English,USA,PG-13,237000000,2009,936,1.78,33000" http://localhost:8080/features/movie_predict

##### Expected Output:
HIT

#### Example 2:
curl -X POST -F "factor=Lawrence Guterman,94,6,227,Traylor Howard,490,17010646,Comedy|Family|Fantasy,Jamie Kennedy,40751,Ben Stein,0,baby|cartoon on tv|cartoonist|dog|mask,239,English,USA,PG,84000000,2005,294,1.85,881" http://localhost:8080/features/movie_predict

##### Expected Output:
AVG

#### Example 3:
curl -X POST -F "factor=Jon M. Chu,115,209,41,Sean Kingston,569,73000942,Documentary|Music,Usher Raymond,74351,Boys II Men,1,boyhood friend|manager|plasma tv|prodigy|star,233,English,USA,G,13000000,2011,69,1.85,62000" http://localhost:8080/features/movie_predict


##### Expected Output:
FLOP