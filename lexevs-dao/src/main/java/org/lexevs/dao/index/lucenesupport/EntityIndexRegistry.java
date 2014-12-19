package org.lexevs.dao.index.lucenesupport;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.springframework.core.io.FileSystemResource;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: m005256
 * Date: 12/18/14
 * Time: 8:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class EntityIndexRegistry {

    public LuceneDirectoryFactory.NamedDirectory getNamedDirectory(AbsoluteCodingSchemeVersionReference codingSchemeVersionReference) {
        return null;
    }

    public LuceneDirectoryFactory.NamedDirectory getAllNamedDirectory(AbsoluteCodingSchemeVersionReference codingSchemeVersionReference) {
        try {
            return (LuceneDirectoryFactory.NamedDirectory) new LuceneDirectoryFactory("test", new FileSystemResource("test"), new DefaultLuceneDirectoryCreator()).getObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String... args) {
        long start = System.nanoTime();

        File file = new File("/Users/m005256/git");
        String[] directories = file.list();

        for(String dir : directories) {
            System.out.println(dir);
        }

        System.out.println(System.nanoTime() - start);

    }

    public void removeNamedDirectory(LuceneDirectoryFactory.NamedDirectory namedDirectory) {
        namedDirectory.remove();
    }
}
