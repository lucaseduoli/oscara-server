package net.minecraft.server.rcon;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RemoteStatusReply {

    private final ByteArrayOutputStream outputStream;
    private final DataOutputStream dataOutputStream;

    public RemoteStatusReply(int i) {
        this.outputStream = new ByteArrayOutputStream(i);
        this.dataOutputStream = new DataOutputStream(this.outputStream);
    }

    public void a(byte[] abyte) throws IOException {
        this.dataOutputStream.write(abyte, 0, abyte.length);
    }

    public void a(String s) throws IOException {
        this.dataOutputStream.writeBytes(s);
        this.dataOutputStream.write(0);
    }

    public void a(int i) throws IOException {
        this.dataOutputStream.write(i);
    }

    public void a(short short0) throws IOException {
        this.dataOutputStream.writeShort(Short.reverseBytes(short0));
    }

    public void b(int i) throws IOException {
        this.dataOutputStream.writeInt(Integer.reverseBytes(i));
    }

    public void a(float f) throws IOException {
        this.dataOutputStream.writeInt(Integer.reverseBytes(Float.floatToIntBits(f)));
    }

    public byte[] a() {
        return this.outputStream.toByteArray();
    }

    public void b() {
        this.outputStream.reset();
    }
}
