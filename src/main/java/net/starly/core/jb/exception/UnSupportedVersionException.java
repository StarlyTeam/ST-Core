package net.starly.core.jb.exception;

public class UnSupportedVersionException extends Exception {

    public UnSupportedVersionException(String version) {
        super(version + " 버전은 지원하지 않는 버전입니다.");
    }

}
