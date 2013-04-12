package structures;

public class DocumentInfo {

	private String url;
	private int documentLength;
	/**
	 * @param url
	 * @param documentLength
	 */
	public DocumentInfo(String url, int documentLength) {
		super();
		this.url = url;
		this.documentLength = documentLength;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the documentLength
	 */
	public int getDocumentLength() {
		return documentLength;
	}
	/**
	 * @param documentLength the documentLength to set
	 */
	public void setDocumentLength(int documentLength) {
		this.documentLength = documentLength;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DocumentInfo [url=" + url + ", documentLength="
				+ documentLength + "]";
	}
	
	
}
