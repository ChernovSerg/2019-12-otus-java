package ru.otus.chernovsa.myjunit;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import static ru.otus.chernovsa.myjunit.TestStatus.FAILED;
import static ru.otus.chernovsa.myjunit.TestStatus.SUCCESSFULLY;

public class TestStatistics {
    private int successfulTests = 0;
    private int failedTests = 0;
    private Map<String, TestStatus> testStatistic = new TreeMap<>(String::compareTo);

    public void addTestResults(String testName, TestStatus status) {
        testStatistic.put(testName, status);
        switch (status) {
            case SUCCESSFULLY:
                successfulTests++;
                break;
            case FAILED:
                failedTests++;
                break;
            default:
                break;
        }
    }

    public String getStatisticsToString() {
        StringBuilder result = new StringBuilder();
        result.append("====================\n");
        result.append("Test results:\n");
        result.append("====================\n");
        result.append("All tests ").append(successfulTests + failedTests).append("\n");
        int successfully = Collections.frequency(testStatistic.values(), SUCCESSFULLY);
        result.append("Successful tests - ").append(successfully).append("\n");
        int failed = Collections.frequency(testStatistic.values(), FAILED);
        result.append("Failed tests - ").append(failed).append("\n");
        result.append("--------------------\n");
        result.append("Detailed statistics:\n");
        result.append("--------------------\n");
        for (Map.Entry<String, TestStatus> entry : testStatistic.entrySet()) {
            result.append(entry.getKey()).append(" - ").append(entry.getValue()).append("\n");
        }
        return result.toString();
    }
}
