
BOOSTPATH=/Users/sbanacho/boost_1_39_0
BOOSTLIB=$(BOOSTPATH)/stage/lib
BOOSTINC=$(BOOSTPATH)

BOOSTREGEX=$(BOOSTLIB)/libboost_regex-gcc40-mt.a

# tested with flex 2.5.4 and 2.5.33
LEX=/usr/bin/flex

# tested with bison 1.28, 1.875c, and 2.3
YACC=/usr/bin/bison
