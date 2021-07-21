## Test cases
- Run main method of PerfProblem class with input value
- See reference data for prime numbers in [primeNumbers](./src/test/resources/primeNumbers.csv)

| Input value      | Expected result          |
| :----:           | :----:                   |
| 2                | 2                        |
| 100              | prime numbers 2 ... 97   |
| 101              | prime numbers 2 ... 101  |
| 10000            | prime numbers 2 ... 9973 |
| null             | empty output             |
| ""               | empty output             |
| 0                | empty output             |
| 1                | empty output             |
| -1               | empty output             |
| "test string"    | empty output             |
| 2.7              | empty output             |