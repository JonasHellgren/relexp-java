package core.foundation.config;

public record PathAndFile(
        String path,
        String name,
        String fileType
) {

    public static PathAndFile of(String path, String name) {
        return new PathAndFile(path,name,"");
    }


    public static PathAndFile xlsxOf(String path, String name) {
        return new PathAndFile(path,name,"xlsx");
    }

    public static PathAndFile xlsOf(String path, String name) {
        return new PathAndFile(path,name,"xls");
    }

    public static PathAndFile csvOf(String path, String name) {
        return new PathAndFile(path,name,"csv");
    }

    public static PathAndFile ofPng(String chartDir, String s) {
        return new PathAndFile(chartDir,s,"png");
    }

    public String fullName() {
        return path+name+"."+fileType;
    }

    public String pathAndName() {
        return path+name;
    }

    }
