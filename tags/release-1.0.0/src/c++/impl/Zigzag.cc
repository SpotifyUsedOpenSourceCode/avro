/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


#include "Zigzag.hh"

namespace avro {

uint64_t 
encodeZigzag64(int64_t input)
{
    return ((input << 1) ^ (input >> 63));
}

int64_t 
decodeZigzag64(uint64_t input)
{
    return ((input >> 1) ^ -(input & 1));
}

uint32_t 
encodeZigzag32(int32_t input)
{
    return ((input << 1) ^ (input >> 31));
}

int32_t 
decodeZigzag32(uint32_t input)
{
    return ((input >> 1) ^ -(input & 1));
}

size_t 
encodeInt64(int64_t input, boost::array<uint8_t, 9> &output)
{
    // get the zigzag encoding 
    uint64_t val = encodeZigzag64(input);

    // put values in an array of bytes with variable length encoding
    const int mask  = 0x7F;
    output[0] = val & mask;
    size_t bytesOut = 1;
    while( val >>=7 ) {
        output[bytesOut++] = (val & mask) | 0x80;
    }

    // arrange array so msb is first
    int head = 0;
    int tail = bytesOut - 1;
    while(head < tail) {
        std::swap(output[head++], output[tail--]);
    }

    return bytesOut;
}

size_t 
encodeInt32(int32_t input, boost::array<uint8_t, 5> &output)
{
    // get the zigzag encoding 
    uint32_t val = encodeZigzag32(input);

    // put values in an array of bytes with variable length encoding
    const int mask  = 0x7F;
    output[0] = val & mask;
    size_t bytesOut = 1;
    while( val >>=7 ) {
        output[bytesOut++] = (val & mask) | 0x80;
    }

    // arrange array so msb is first
    int head = 0;
    int tail = bytesOut - 1;
    while(head < tail) {
        std::swap(output[head++], output[tail--]);
    }

    return bytesOut;
}

} // namespace avro
