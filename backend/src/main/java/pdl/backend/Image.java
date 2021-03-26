package pdl.backend;

public class Image {
  private static Long count = Long.valueOf(0);
  private Long id;
  private String name;
  private byte[] data;
  private org.springframework.http.MediaType type;
  private long[] dimensions;

  public void setId(Long id) {
    this.id = id;
  }
  public void setData(byte[] data) {
    this.data = data;
  }

  public org.springframework.http.MediaType getType() {
    return this.type;
  }

  public void setType(org.springframework.http.MediaType type) {
    this.type = type;
  }

  public long[] getDimensions() {
    return this.dimensions;
  }

  public void setDimensions(long[] dimensions) {
    this.dimensions = dimensions;
  }

  public Image(final String name, final byte[] data, final long[] dimensions, org.springframework.http.MediaType type) {
    id = count++;
    this.name = name;
    this.data = data;
    this.dimensions=dimensions;
    this.type=type;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public byte[] getData() {
    return data;
  }
}
