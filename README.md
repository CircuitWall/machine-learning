# Machine-learning libs by circuitwall.com
Requirements:
Java 8 and above (stream/parallel stream, default)

## Algorithms

```xml
<dependency>
    <groupId>com.circuitwall.ml</groupId>
    <artifactId>algorithm</artifactId>
    <version>1.1.1</version>
</dependency>
```

This package contains key logics for the algorithm to run.

### Evolution Algorithm
"In artificial intelligence, an evolutionary algorithm (EA) is a subset of evolutionary computation, a generic population-based metaheuristic optimization algorithm. An EA uses mechanisms inspired by biological evolution, such as reproduction, mutation, recombination, and selection.
Evolutionary algorithm" - Wikipedia

## Platforms

By including following packages, the algorithm can be run on different environment.

### Flink

A DAG engine read more here <https://flink.apache.org>

```xml
<dependency>
    <groupId>com.circuitwall.ml</groupId>
    <artifactId>platform-flink</artifactId>
    <version>1.1.1</version>
</dependency>
```
