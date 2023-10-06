FROM sitespeedio/node:ubuntu-20.04-nodejs-16.16.0

RUN apt-get update && apt-get install -y default-jre default-jdk maven unzip vim wget curl

RUN apt-get install -y wget libfreetype6 libfreetype6-dev build-essential chrpath libssl-dev libxft-dev libfontconfig1 libfontconfig1-dev

RUN wget https://bitbucket.org/ariya/phantomjs/downloads/phantomjs-2.1.1-linux-x86_64.tar.bz2 

RUN tar xvjf phantomjs-2.1.1-linux-x86_64.tar.bz2 -C /usr/local/share/ 

RUN ln -sf /usr/local/share/phantomjs-2.1.1-linux-x86_64/bin/phantomjs /usr/local/bin 

COPY setup.sh /root

RUN apt-get install -y git

RUN git clone https://github.com/codete/QAcheck


