# hello-world-reactor

The objective of this repo is to reproduce the Pool#acquire(Duration) time out error



Steps to reproduce:

- Start the application

- Hit the url http://localhost:8080/hello multiple times (OR) Run FooTest

This is an attempt to simulate the following scenario:

- Once the request is received , request is acknowledged
- Some processing happens in a different thread (Response of this process is not sent to the caller. It is intentional)
- Creates a flux from a list(based on the request) and calls an API for each item in the list 
- Failure happens when 3/4 requests land simulatenously.

- Compelte log is present in log.txt
