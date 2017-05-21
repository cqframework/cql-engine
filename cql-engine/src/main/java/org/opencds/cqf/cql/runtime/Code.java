package org.opencds.cqf.cql.runtime;

/**
 * Created by Bryn on 4/15/2016.
 */
public class Code {

    private String code;
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public Code withCode(String code) {
        setCode(code);
        return this;
    }

    private String display;
    public String getDisplay() {
        return display;
    }
    public void setDisplay(String display) {
        this.display = display;
    }
    public Code withDisplay(String display) {
        setDisplay(display);
        return this;
    }

    private String system;
    public String getSystem() {
        return system;
    }
    public void setSystem(String system) {
        this.system = system;
    }
    public Code withSystem(String system) {
        setSystem(system);
        return this;
    }

    private String version;
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public Code withVersion(String version) {
        setVersion(version);
        return this;
    }

    public Boolean equal(Code other) {
        return this.getCode().equals(other.getCode())
                && ((this.getSystem() == null && other.getSystem() == null)
                || (this.getSystem() != null && this.getSystem().equals(other.getSystem())))
                && ((this.getVersion() == null && other.getVersion() == null)
                || (this.getVersion() != null && this.getVersion().equals(other.getVersion())));
    }

    @Override
    public String toString() {
        return String.format(
                "Code { code: %s, system: %s, version: %s, display: %s }",
                getCode(), getSystem(), getVersion(), getDisplay()
        );
    }

}
