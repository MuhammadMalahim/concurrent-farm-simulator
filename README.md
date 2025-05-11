# Concurrent Farm Simulator

This project is a multithreaded Java simulation that models interactions between dogs and sheep on a farm. It demonstrates concurrent programming concepts using Java’s `ExecutorService` and atomic operations to simulate real-time activity and control.

## Features

- **Multithreaded Simulation**: Uses `ExecutorService` for efficient and concurrent task execution.
- **Atomic State Control**: Uses atomic flags (e.g., `AtomicBoolean`) to manage simulation start/stop dynamically.
- **Real-Time Updates**: Provides real-time simulation status of sheep and dog interactions.
- **Graceful Shutdown**: Ensures threads are properly terminated with resource cleanup.
- **Modular & Extensible**: Designed with extensibility in mind for adding more animal types or interaction logic.

## Core Concepts

- Java Concurrency (`ExecutorService`, `Runnable`, `AtomicBoolean`)
- Thread-safe status monitoring
- Clean multithreaded architecture
- Encapsulation of farm logic

## Project Structure

- `Farm.java`: Core simulation logic and state control.
- `Dog.java`: Represents a dog thread that interacts with sheep.
- `Sheep.java`: Represents a sheep that reacts to dog behavior.
- `Main.java`: Entry point to the simulation.
- `utils/`: Utility classes and shared resources.
