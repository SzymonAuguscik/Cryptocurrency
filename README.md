# Cryptocurrency
Inspired by https://www.youtube.com/watch?v=bBC-nXj3Ng4&t=998s I've decided to prepare a Java app that ilustrates the
phenomena presented in video and "invent" my own cryptocurrency named after one of my favourite fantasy protagonists.
## Contents
### Market
Main object in simulation. It consists of participants, transactions and
ledgers. Initial data is created automatically.
### Ledger
List of transactions. It stores all transaction authorized in certain time
and is secured with 256-SHA sign depending on ledger's body.
### Transaction
Registered cash flow between two participants. It must be signed by sender.
### Participant
A trader. Everyone can send and get limited amount of money but being
in the red is forbidden.
## How to run
### Using existing market
java -jar Geraltium.jar
### Using new market
download repo
run "main()" in Market class
create a JAR file
