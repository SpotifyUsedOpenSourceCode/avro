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
package org.apache.avro.file;

import java.util.HashMap;
import java.util.Map;
import java.util.zip.Deflater;

import org.apache.avro.AvroRuntimeException;

/** Encapsulates the ability to specify and configure a compression codec. */
public abstract class CodecFactory {
  /** Null codec, for no compression. */
  public static CodecFactory nullCodec() { 
    return NullCodec.OPTION; 
  };
  
  /** Deflate codec, with specific compression.
   * compressionLevel should be between 1 and 9, inclusive. */
  public static CodecFactory deflateCodec(int compressionLevel) { 
    return new DeflateCodec.Option(compressionLevel); 
  };
  
  /** Creates internal Codec. */
  protected abstract Codec createInstance();
  
  /** Mapping of string names (stored as metas) and codecs. 
   * Note that currently options (like compression level)
   * are not recoverable. */
  private static final Map<String, CodecFactory> REGISTERED = 
    new HashMap<String, CodecFactory>();

  private static final int DEFAULT_DEFLATE_LEVEL = Deflater.DEFAULT_COMPRESSION;

  static {
    addCodec("null", nullCodec());
    addCodec("deflate", deflateCodec(DEFAULT_DEFLATE_LEVEL));
  }

  /** Maps a codec name into a CodecOption. */
  public static CodecFactory fromString(String s) {
    CodecFactory o = REGISTERED.get(s);
    if (o == null) {
      throw new AvroRuntimeException("Unrecognized codec: " + s);
    }
    return o;
  }
  
  /** Adds a new codec implementation.  If name already had
   * a codec associated with it, returns the previous codec. */
  public static CodecFactory addCodec(String name, CodecFactory c) {
    return REGISTERED.put(name, c);
  }
  
  @Override
  public String toString() {
    Codec instance = this.createInstance();
    return instance.toString();
  }
  
}
