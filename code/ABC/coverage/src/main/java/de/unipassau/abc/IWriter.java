package de.unipassau.abc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public interface IWriter {
    void toFile(final Coverage coverage, boolean ignoreUncovered) throws IOException;

    void toDir(final Coverage coverage, boolean ignoreUncovered) throws IOException;

    default void toFile(final Coverage coverage) throws IOException {
        toFile(coverage, false);
    }

    default void toDir(final Coverage coverage) throws IOException {
        toDir(coverage, false);
    }

    class TextWriter implements IWriter {

        private File file;

        public TextWriter(File file) {
            this.file = file;
        }

        @Override
        public void toFile(Coverage coverage, boolean ignoreUncovered) throws IOException {
            File parent = file.getParentFile();
            if (parent != null) {
                parent.mkdirs();
            }

            PrintStream out = new PrintStream(new FileOutputStream(file));
            for (Entry<String, Map<Integer, Integer>> entry : coverage.get().entrySet()) {
                String className = entry.getKey();

                if (!Filters.isAllowedClass(className)) {
                    continue;
                }

                Map<Integer, Integer> coverageData = entry.getValue();

                if (ignoreUncovered) {
                    coverageData = coverageData.entrySet().stream().filter(e -> e.getValue() != 0).collect(
                          Collectors.toMap(Entry::getKey, Entry::getValue));
                    if (coverageData.isEmpty()) {
                        continue;
                    }
                }

                out.printf(">> %s%n", className);

                // Output the results sorted
                List<Integer> sortedLineNumbers = new ArrayList<>(coverageData.keySet());
                Collections.sort(sortedLineNumbers);

                for (Integer key : sortedLineNumbers) {
                    int count = coverageData.get(key);
                    out.printf("%d %d%n", key, count);
                }
            }
        }

        @Override
        public void toDir(Coverage coverage, boolean ignoreUncovered) throws IOException {
            file.mkdirs();

            for (Entry<String, Map<Integer, Integer>> entry : coverage.get().entrySet()) {
                String className = entry.getKey();

                if (!Filters.isAllowedClass(className)) {
                    continue;
                }

                String fileName = className.replace("/", ".");
                Path file = Paths.get(this.file.getAbsolutePath(), fileName);
                PrintStream out = new PrintStream(new FileOutputStream(file.toFile()));

                Map<Integer, Integer> coverageData = entry.getValue();

                if (ignoreUncovered) {
                    coverageData = coverageData.entrySet().stream().filter(e -> e.getValue() != 0).collect(
                          Collectors.toMap(Entry::getKey, Entry::getValue));
                    if (coverageData.isEmpty()) {
                        continue;
                    }
                }

                // Output the results sorted
                List<Integer> sortedLineNumbers = new ArrayList<Integer>(coverageData.keySet());
                Collections.sort(sortedLineNumbers);

                for (Integer key : sortedLineNumbers) {
                    int count = coverageData.get(key);
                    out.printf("%d %d%n", key, count);
                }
            }
        }
    }

}
