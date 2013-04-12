/**
 * 
 */
package parsing;

/**
 * @author Darshan
 *
 */
public class Page {
    private String url,page,status;

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
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
     * @return the page
     */
    public String getPage() {
        return page;
    }

    /**
     * @param page the page to set
     */
    public void setPage(String page) {
        this.page = page;
    }

    /**
     * @param url
     * @param page
     * @param status
     */
    public Page(String url, String page, String status) {
	super();
	this.url = url;
	this.page = page;
	this.status = status;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "Page [url=" + url + ", page=" + page + ", status=" + status
		+ "]";
    }

    
}
