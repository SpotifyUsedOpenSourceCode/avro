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
package org.apache.avro.tool;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.List;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import org.apache.avro.Schema;
import org.apache.avro.file.CodecFactory;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericDatumWriter;

/** Reads a text file into an Avro data file.
 * 
 * Can accept a file name, and HDFS file URI, or stdin. Can write to a file
 * name, an HDFS URI, or stdout.*/
public class FromTextTool implements Tool {
  private static final String TEXT_FILE_SCHEMA = 
    "\"bytes\"";
  
  @Override
  public String getName() {
    return "fromtext";
  }

  @Override
  public String getShortDescription() {
    return "Imports a text file into an avro data file.";
  }

  @Override
  public int run(InputStream stdin, PrintStream out, PrintStream err,
      List<String> args) throws Exception {
    
    OptionParser p = new OptionParser();
    OptionSpec<Integer> level = p.accepts("level", "compression level")
    .withOptionalArg().ofType(Integer.class);

    OptionSet opts = p.parse(args.toArray(new String[0]));

    if (opts.nonOptionArguments().size() != 2) {
      err.println("Expected 2 args: from_file to_file (local filenames," +
          " Hadoop URI's, or '-' for stdin/stdout");
      p.printHelpOn(err);
      return 1;
    }
 
    BufferedInputStream inStream = Util.fileOrStdin(args.get(0), stdin);
    BufferedOutputStream outStream = Util.fileOrStdout(args.get(1), out);
    
    int compressionLevel = 1; // Default compression level
    if (opts.hasArgument(level)) {
      compressionLevel = level.value(opts);
    }
  
    BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
    DataFileWriter<ByteBuffer> writer =
        new DataFileWriter<ByteBuffer>(new GenericDatumWriter<ByteBuffer>());
    writer.setCodec(CodecFactory.deflateCodec(compressionLevel));
    writer.create(Schema.parse(TEXT_FILE_SCHEMA), outStream);

    String line;
    while((line = reader.readLine()) != null) {
      ByteBuffer buff = ByteBuffer.wrap(line.getBytes());
      writer.append(buff);
    }
    
    writer.flush();
    writer.close();
    inStream.close();
    return 0;
  }

}
