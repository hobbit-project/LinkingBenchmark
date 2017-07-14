# Linking Benchmark

Linking Benchmark generator is based on SPIMBENCH, to test the performance of Instance Matching tools that implement string-based approaches for identifying matching entities.

# Uploading Benchmark to HOBBIT Platform
Guidelines on how to upload a benchmark can be found here: https://github.com/hobbit-project/platform/wiki/Benchmark-your-system

# Running the Benchmark
If you want to run Linking Benchmark, please follow the guidelines found here: https://github.com/hobbit-project/platform/wiki/Experiments

**Description of Linking Benchmark parameters**:
* Generated data format
* Seed for mimicking algorithm: The seed value for a mimicking algorithm. 
* Percentage of value-based transformations of labels of the source dataset. The sum of percentages should be smaller or equal to 1.0 and NOTRANSFORMATION should not be 0.0. Percentages define: BLANKCHARSADDITION , BLANKCHARSDELETION ,RANDOMCHARSADDITION , RANDOMCHARSDELETION , RANDOMCHARSMODIFIER, TOKENADDITION, TOKENDELETION, TOKENSHUFFLE, NAMESTYLEABBREVIATION, NOTRANSFORMATION
* Percentage of timestamp format transformations.
* Percentage of points to labels transformation.
* Population of generated data (number on instances)
* Percentage of points to keep from source trace: one can restrict the number of points to consider due to the possibly very large number of points per Trace
* Percentage of Addition/Deletion of points. The sum of percentages should be smaller or equal to 1.0 and NOTRANSFORMATION should not be 0.0. Percentages define: ADDPOINTS, REMOVEPOINTS, NOTRANSFORMATION
* Percentage of transformings points to labels or change point format (for the target dataset). The sum of percentages should be smaller or equal to 1.0 and NOTRANSFORMATION should not be 0.0. Percentages define: CHANGECOORDINATES, POINTSTOLABEL, NOTRANSFORMATION
* Difficulty of the transformations percentage.
