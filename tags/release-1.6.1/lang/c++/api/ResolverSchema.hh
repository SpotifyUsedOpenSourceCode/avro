/*
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

#ifndef avro_ResolverSchema_hh__
#define avro_ResolverSchema_hh__

#include <boost/noncopyable.hpp>
#include <boost/shared_ptr.hpp>
#include <stdint.h>
#include "Boost.hh"
#include "Reader.hh"

/// \file ResolverSchema.hh
///

namespace avro {
    
class ValidSchema;
class Layout;
class Resolver;

class ResolverSchema {

  public:

    ResolverSchema(const ValidSchema &writer, const ValidSchema &reader, const Layout &readerLayout);

  private:

    friend class ResolvingReader;

    void parse(Reader &reader, uint8_t *address); 

    boost::shared_ptr<Resolver> resolver_;

};

} // namespace avro

#endif