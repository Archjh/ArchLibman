package me.wavelength.baseclient.thealtening;

import com.google.common.collect.Sets;
import me.wavelength.baseclient.event.events.Render2DEvent;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Set;

public class Utilities {

	private static final Utilities INSTANCE = new Utilities();

	private static final int DEFAULT_BUFFER_SIZE = 8192;

	private static final int MAX_BUFFER_SIZE = 2147483639;

	private Set<Render2DEvent> unregRenderers = Sets.newHashSet();

	public byte[] readAllBytes(InputStream inputStream) throws IOException {
		byte[] buf = new byte[DEFAULT_BUFFER_SIZE];
		int capacity = buf.length;
		int nread = 0;
		while (true) {
			int n;
			while ((n = inputStream.read(buf, nread, capacity - nread)) > 0)
				nread += n;
			if (n < 0)
				break;
			if (capacity <= MAX_BUFFER_SIZE - capacity) {
				capacity <<= 1;
			} else {
				if (capacity == MAX_BUFFER_SIZE)
					throw new OutOfMemoryError("Required array size too large");
				capacity = MAX_BUFFER_SIZE;
			}
			buf = Arrays.copyOf(buf, capacity);
		}
		return (capacity == nread) ? buf : Arrays.copyOf(buf, nread);
	}

	public static Utilities getInstance() {
		return INSTANCE;
	}

	public Set<Render2DEvent> getUnregisteredRenderers() {
		return unregRenderers;
	}
}