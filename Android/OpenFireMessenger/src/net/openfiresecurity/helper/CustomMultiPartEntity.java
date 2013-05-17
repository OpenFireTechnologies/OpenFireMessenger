/*
 * Copyright (c) 2013. Alexander Martinz @ OpenFire Security
 */

package net.openfiresecurity.helper;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;

public class CustomMultiPartEntity extends MultipartEntity {

	private final ProgressListener listener;

	public CustomMultiPartEntity(final ProgressListener listener) {
		super();
		this.listener = listener;
	}

	public CustomMultiPartEntity(final HttpMultipartMode mode,
			final ProgressListener listener) {
		super(mode);
		this.listener = listener;
	}

	public CustomMultiPartEntity(HttpMultipartMode mode, final String boundary,
			final Charset charset, final ProgressListener listener) {
		super(mode, boundary, charset);
		this.listener = listener;
	}

	@Override
	public void writeTo(final OutputStream outstream) throws IOException {
		super.writeTo(new CountingOutputStream(outstream, listener));
	}

	public static interface ProgressListener {
		void transferred(long num);
	}

	public static class CountingOutputStream extends FilterOutputStream {

		private final ProgressListener listener;
		private long transferred;

		public CountingOutputStream(final OutputStream out,
				final ProgressListener listener) {
			super(out);
			this.listener = listener;
			transferred = 0;
		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			int BUFFER_SIZE = 10000;
			int chunkSize;
			int currentOffset = 0;

			while (len > currentOffset) {
				chunkSize = len - currentOffset;
				if (chunkSize > BUFFER_SIZE) {
					chunkSize = BUFFER_SIZE;
				}
				out.write(b, currentOffset, chunkSize);
				currentOffset += chunkSize;
				transferred += chunkSize;
				listener.transferred(transferred);
			}
		}

		@Override
		public void write(int b) throws IOException {
			out.write(b);
			transferred++;
			listener.transferred(transferred);
		}
	}
}
