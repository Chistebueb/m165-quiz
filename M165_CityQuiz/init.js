db = db.getSiblingDB('cityDB');

db.createCollection('cities');

db.cities.insertMany([
        {
            "name": "London",
            "population": 8866180,
            "populationDensity": 5597,
            "area": 1572,
            "avgAge": 36,
            "gdpPpp": 57157
        },
        {
            "name": "Zürich",
            "population": 447521,
            "populationDensity": 4655,
            "area": 87,
            "avgAge": 41,
            "gdpPpp": 68761
        },
        {
            "name": "Berlin",
            "population": 3770800,
            "populationDensity": 4200,
            "area": 891,
            "avgAge": 42,
            "gdpPpp": 40000
        },
        {
            "name": "Oslo",
            "population": 709037,
            "populationDensity": 1561,
            "area": 454,
            "avgAge": 37,
            "gdpPpp": 69167
        },
        {
            "name": "Warschau",
            "population": 1861975,
            "populationDensity": 3469,
            "area": 517,
            "avgAge": 40,
            "gdpPpp": 48681
        },
        {
            "name": "Moskau",
            "population": 12455682,
            "populationDensity": 4583,
            "area": 2510,
            "avgAge": 38,
            "gdpPpp": 45803
        },
        {
            "name": "Tokyo",
            "population": 9640742,
            "populationDensity": 15351,
            "area": 628,
            "avgAge": 49,
            "gdpPpp": 43664
        },
        {
            "name": "Denver",
            "population": 682545,
            "populationDensity": 1801,
            "area": 400,
            "avgAge": 36,
            "gdpPpp": 61795
        },
        {
            "name": "New York",
            "population": 8550405,
            "populationDensity": 10356,
            "area": 1214,
            "avgAge": 37,
            "gdpPpp": 69915
        },
        {
            "name": "Miami",
            "population": 441003,
            "populationDensity": 4322,
            "area": 143,
            "avgAge": 40,
            "gdpPpp": 44480
        }
    ]
);