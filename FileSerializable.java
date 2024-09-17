import java.io.Serializable;
import java.util.Date;

public class FileSerializable implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name, path;
	private byte[] data;
	private Date lastModifiedDate;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public FileSerializable(String name, String path, byte[] data, Date lastModifiedDate) {
		this.name = name;
		this.path = path;
		this.data = data;
		this.lastModifiedDate = lastModifiedDate;
	}

	public FileSerializable() {
	}

}
