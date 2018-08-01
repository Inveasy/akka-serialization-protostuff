/*
 * Copyright 2018 Guillaume Gravetot
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.inveasy.cluster.serialization;

import akka.serialization.ByteBufferSerializer;
import akka.serialization.SerializerWithStringManifest;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.nio.ByteBuffer;

public class ProtostuffSerializer extends SerializerWithStringManifest implements ByteBufferSerializer
{
	private ThreadLocal<LinkedBuffer> localBuffer = ThreadLocal.withInitial(() -> LinkedBuffer.allocate(4096));
	
	@Override
	public int identifier()
	{
		return 451;
	}
	
	@Override
	public String manifest(Object o)
	{
		return o.getClass().getTypeName();
	}
	
	@Override
	public Object fromBinary(byte[] bytes, String manifest)
	{
		return fromBinary(ByteBuffer.wrap(bytes), manifest);
	}
	
	@Override
	public byte[] toBinary(Object o)
	{
		LinkedBuffer buffer = localBuffer.get();
		try
		{
			return ProtostuffIOUtil.toByteArray(o, (Schema)RuntimeSchema.getSchema(o.getClass()), buffer);
			// return ProtostuffIOUtil.toByteArray(o, canonicalNameToSchema(o.getClass().getCanonicalName()), buffer);
		}
		finally
		{
			buffer.clear();
		}
	}
	
	@Override
	public void toBinary(Object o, ByteBuffer buf)
	{
		LinkedBuffer buffer = localBuffer.get();
		try
		{
			buf.put(ProtostuffIOUtil.toByteArray(o, (Schema)RuntimeSchema.getSchema(o.getClass()), buffer));
			// buf.put(ProtostuffIOUtil.toByteArray(o, canonicalNameToSchema(o.getClass().getCanonicalName()), buffer));
		}
		finally
		{
			buffer.clear();
		}
	}
	
	@Override
	public Object fromBinary(ByteBuffer buf, String manifest)
	{
		Schema schema = null;
		try
		{
			schema = RuntimeSchema.getSchema(ClassLoader.getSystemClassLoader().loadClass(manifest));
		} catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		
		byte[] arr = new byte[buf.remaining()];
		buf.get(arr);
		
		Object o = schema.newMessage();
		ProtostuffIOUtil.mergeFrom(arr, o, schema);
		
		return o;
	}
}
