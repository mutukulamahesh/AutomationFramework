package com.framework.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.internal.Utils;
import org.testng.xml.XmlSuite;

import com.beust.jcommander.internal.Lists;

public class CustomReport implements IReporter {

    private static final Logger LOG = Logger.getLogger(CustomReport.class);
    private static String timeZone = "IST";
    private static SimpleDateFormat sdfdate = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
    private static SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm:ss a");
    private static String outFilename = "OperaCloud.html";
    private static NumberFormat integerFormat = NumberFormat.getIntegerInstance();
    private static NumberFormat decimalFormat = NumberFormat.getNumberInstance();
    protected PrintWriter writer;
    protected List<SuiteResult> suiteResults = Lists.newArrayList();
    private StringBuilder buffer = new StringBuilder();


    File fd = new File("");
    String date;
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss");
    String folderNameWithTimeStamp = this.df.format(new Date());
    String currentDir = System.getProperty("user.dir") + "//Reports//";
    String finalPath = this.currentDir + this.folderNameWithTimeStamp;

    @Override
    public void generateReport(final List<XmlSuite> xmlSuites, final List<ISuite> suites, final String outputDirectory) {
        try {
            this.writer = createWriter(outputDirectory);
        } catch (IOException e) {
            CustomReport.LOG.error("Unable to create output file", e);
            return;
        }
        for (ISuite suite : suites) {
            this.suiteResults.add(new SuiteResult(suite));
        }
        try {
            writeDocumentStart();
            writeHead();
            writeLogo();
            writeBody();
            writeDocumentEnd();

            this.writer.close();
        } catch (Exception e) {
        }
    }

    protected PrintWriter createWriter(final String outdir) throws IOException {
        new File(outdir).mkdirs();
        return new PrintWriter(new BufferedWriter(new FileWriter(new File(outdir, CustomReport.outFilename))));
    }

    protected void writeDocumentStart() {
        this.writer
            .println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");
        this.writer.print("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
    }

    protected void writeHead() {
        this.writer.print("<head>");
        this.writer.print("<title> Opera Cloud Test Automation Execution Report</title>");
        writeStylesheet();
        this.writer.print("</head>");

    }

    protected void writeLogo() throws Exception {

    	//com.oracle.hgbu.opera.qaauto.ui.utilities.Utils utils = new com.oracle.hgbu.opera.qaauto.ui.utilities.Utils();
    	//System.out.println("opera version :: "+ com.oracle.hgbu.opera.qaauto.ui.utilities.Utils.OPERAVersion);
    	//System.out.println("opera version :: "+ com.oracle.hgbu.opera.qaauto.ui.utilities.Utils.OPERAUIGITHash);    	
    	//System.out.println("opera version :: "+ com.oracle.hgbu.opera.qaauto.ui.utilities.Utils.OPERAWSGITHash);
    	
    	this.writer.print("<table cellpadding=0 cellspacing=0 border=0>");       
        this.writer.print("<th>Executed By</th>");
        this.writer.print("<th>OPERA Version</th>");
        this.writer.print("<th>OPERA UI GIT Hash</th>");
        this.writer.print("<th>OPERA WS GIT Hash</th>");
        this.writer.print("<th>For any queries, Please contact</th>");
        this.writer.print("<tr>");        
        this.writer.print("<td align='right'> " + getDateAsString() + " " + " | " + getSysInfoAsString() + "</td>");       
        this.writer.print("<td align='right'> " + ReadValueFromPropertiesFile("OPERAVersion","ExecutionTime.properties") + "</td>");       
        this.writer.print("<td align='right'> " + ReadValueFromPropertiesFile("OPERAUIGITHash","ExecutionTime.properties") + "</td>");       
        this.writer.print("<td align='right'> " + ReadValueFromPropertiesFile("OPERAWSGITHash","ExecutionTime.properties") + "</td>");
        this.writer.print("<td align='right'>opera_qa_automation_architects_us_grp@oracle.com </td>");
        this.writer.print("</tr>");
        this.writer.print("</table>");

    }

    protected void writeStylesheet() {
        this.writer.print("<style type=\"text/css\">");
        this.writer.print("table {margin-bottom:10px;border-collapse:collapse;empty-cells:show}");
        this.writer.print("th,td {border:1px solid #009;padding:.25em .5em}");
        this.writer.print("th {vertical-align:bottom}");
        this.writer.print("td {vertical-align:top}");
        this.writer.print("table a {font-weight:bold}");
        this.writer.print(".stripe td {background-color: #E6EBF9}");
        this.writer.print(".num {text-align:right}");
        this.writer.print(".passedodd td {background-color: #3F3}");
        this.writer.print(".passedeven td {background-color: #0A0}");
        this.writer.print(".skippedodd td {background-color: #DDD}");
        this.writer.print(".skippedeven td {background-color: #CCC}");
        this.writer.print(".failedodd td,.attn {background-color: #F33}");
        this.writer.print(".failedeven td,.stripe .attn {background-color: #D00}");
        this.writer.print(".stacktrace {white-space:pre;font-family:monospace}");
        this.writer.print(".totop {font-size:85%;text-align:center;border-bottom:2px solid #000}");
        this.writer.print("</style>");

    }

    protected void writeBody() throws UnknownHostException {
        this.writer.print("<body>");
        writeReportTitle(" Opera Cloud Test Automation Execution Report");
        writeSuiteSummary();
        writeScenarioSummary();
        //writeScenarioDetails();
        this.writer.print("</body>");
    }

    protected void writeReportTitle(final String title) {
        //this.writer.print("<center><h3>" + title + " - " + getDateAsString() + "</h3></center>");
    	this.writer.print("<center><h3>" + title + "</h3></center>");
    }

    protected void writeDocumentEnd() {
        this.writer.print("</html>");
    }

    protected void writeSuiteSummary() {

        int totalPassedTests = 0;
        int totalSkippedTests = 0;
        int totalFailedTests = 0;
        long totalDuration = 0;
        this.writer.print("<center><b>Overall Module Wise Test Execution Summary</b></center>");
        this.writer.print("<table align='center'>");
        for (SuiteResult suiteResult : this.suiteResults) {
            this.writer.print("<tr><th colspan=\"5\">");
            this.writer.print(Utils.escapeHtml(suiteResult.getSuiteName()));
            this.writer.print("</th></tr>");
        }
        this.writer.print("<tr>");
        this.writer.print("<th>Test</th>");
        this.writer.print("<th># Passed</th>");
        this.writer.print("<th># Skipped</th>");
        this.writer.print("<th># Failed</th>");
        this.writer.print("<th>Duration</th>");
        //this.writer.print("<th>Included Groups</th>");
        //this.writer.print("<th>Excluded Groups</th>");
        this.writer.print("</tr>");

        int testIndex = 0;
        for (SuiteResult suiteResult : this.suiteResults) {
            
            for (TestResult testResult : suiteResult.getTestResults()) {
                int passedTests = testResult.getPassedTestCount();
                int skippedTests = testResult.getSkippedTestCount();
                int failedTests = testResult.getFailedTestCount();
                long duration = testResult.getDuration();
                duration = (long)(millisecondsToSeconds(duration)/60);
                
                this.writer.print("<tr");
                if ((testIndex % 2) == 1) {
                    this.writer.print(" class=\"stripe\"");
                }
                this.writer.print(">");

                this.buffer.setLength(0);
                writeTableData(this.buffer.append("<a href=\"#t").append(testIndex).append("\">")
                    .append(Utils.escapeHtml(testResult.getTestName())).append("</a>").toString());
                writeTableData(CustomReport.integerFormat.format(passedTests), "num");
                writeTableData(CustomReport.integerFormat.format(skippedTests), (skippedTests > 0 ? "num attn" : "num"));
                writeTableData(CustomReport.integerFormat.format(failedTests), (failedTests > 0 ? "num attn" : "num"));
                writeTableData((CustomReport.decimalFormat.format(duration))+" Min(s)", "num");
                //writeTableData(testResult.getIncludedGroups());   (duration / 60)%60;
                //writeTableData(testResult.getExcludedGroups());

                this.writer.print("</tr>");

                totalPassedTests += passedTests;
                totalSkippedTests += skippedTests;
                totalFailedTests += failedTests;
                totalDuration += duration;

                testIndex++;
            }
        }

        // Print totals if there was more than one test
        if (testIndex >= 1) {
            this.writer.print("<tr>");
            this.writer.print("<th>Total</th>");
            writeTableHeader(CustomReport.integerFormat.format(totalPassedTests), "num");
            writeTableHeader(CustomReport.integerFormat.format(totalSkippedTests), (totalSkippedTests > 0 ? "num attn" : "num"));
            writeTableHeader(CustomReport.integerFormat.format(totalFailedTests), (totalFailedTests > 0 ? "num attn" : "num"));
            writeTableHeader((CustomReport.decimalFormat.format(totalDuration))+" Min(s)", "num");
            //this.writer.print("<th colspan=\"2\"></th>");
            this.writer.print("</tr>");

        }

        this.writer.print("</table>");

    }

    /**
     * Writes a summary of all the test scenarios.
     */
    protected void writeScenarioSummary() {
        this.writer.print("<b align=\"center\">Result Summary Table</b>");
        this.writer.print("<table>");
        this.writer.print("<thead>");
        this.writer.print("<tr>");
        this.writer.print("<th>Module</th>");
        //this.writer.print("<th>Method</th>");
        this.writer.print("<th>Test Name</th>");
        this.writer.print("<th>Start Time</th>");
        this.writer.print("<th>Duration</th>");
        this.writer.print("</tr>");
        this.writer.print("</thead>");

        int testIndex = 0;
        int scenarioIndex = 0;
        for (SuiteResult suiteResult : this.suiteResults) {
            this.writer.print("<tbody><tr><th colspan=\"4\">");
            this.writer.print(Utils.escapeHtml(suiteResult.getSuiteName()));
            this.writer.print("</th></tr></tbody>");

            for (TestResult testResult : suiteResult.getTestResults()) {
                this.writer.print("<tbody id=\"t");
                this.writer.print(testIndex);
                this.writer.print("\">");

                String testName = Utils.escapeHtml(testResult.getTestName());

                scenarioIndex += writeScenarioSummary(testName + " &#8212; failed (configuration methods)",testResult.getFailedConfigurationResults(), "failed", scenarioIndex);
                    
                scenarioIndex += writeScenarioSummary(testName + " &#8212; failed", testResult.getFailedTestResults(), "failed", scenarioIndex);
                    
                scenarioIndex += writeScenarioSummary(testName + " &#8212; skipped (configuration methods)",testResult.getSkippedConfigurationResults(), "skipped", scenarioIndex);
                    
                scenarioIndex += writeScenarioSummary(testName + " &#8212; skipped", testResult.getSkippedTestResults(), "skipped", scenarioIndex);
                   
                scenarioIndex +=  writeScenarioSummary(testName + " &#8212; passed", testResult.getPassedTestResults(), "passed", scenarioIndex);
                   

                this.writer.print("</tbody>");

                testIndex++;
            }
        }

        this.writer.print("</table>");
    }

    /**
     * Writes the scenario summary for the results of a given state for a
     * single test.
     */
    private int writeScenarioSummary(final String description, final List<ClassResult> classResults, final String cssClassPrefix,final int startingScenarioIndex) {
        
        int scenarioCount = 0;
        if (!classResults.isEmpty()) {
            this.writer.print("<tr><th colspan=\"4\">");
            this.writer.print(description);
            this.writer.print("</th></tr>");

            int scenarioIndex = startingScenarioIndex;
            int classIndex = 0;
            for (ClassResult classResult : classResults) {
                String cssClass = cssClassPrefix + ((classIndex % 2) == 0 ? "even" : "odd");

                this.buffer.setLength(0);

                int scenariosPerClass = 0;
                int methodIndex = 0;
                for (MethodResult methodResult : classResult.getMethodResults()) {
                    List<ITestResult> results = methodResult.getResults();
                    int resultsCount = results.size();
                    assert resultsCount > 0;

                    ITestResult aResult = results.iterator().next();
                    String methodName = Utils.escapeHtml(aResult.getMethod().getMethodName());
                    long start = aResult.getStartMillis();
                    long duration = aResult.getEndMillis() - start;
                    duration = (long)millisecondsToSeconds(duration)/60;
                
                    
                    // The first method per class shares a row with the class
                    // header
                    if (methodIndex > 0) {
                        this.buffer.append("<tr class=\"").append(cssClass).append("\">");

                    }
                    
                    // Write the timing information with the first scenario per
                    // method
                    this.buffer
                        .append("<td rowspan=\"1\">" +aResult.getName() + "</td>").append("<td rowspan=\"").append(resultsCount) //aResult.getName()
                        .append("\">").append(parseUnixTimeToTimeOfDay(start)).append("</td>").append("<td rowspan=\"")
                        .append(resultsCount).append("\">")
                        .append(CustomReport.decimalFormat.format(duration)+" Min(s)").append("</td></tr>");
                    scenarioIndex++;

                    // Write the remaining scenarios for the method
                    for (int i = 1; i < resultsCount; i++) {
                        this.buffer.append("<tr class=\"").append(cssClass).append("\">").append("<td><a href=\"#m")
                            .append(scenarioIndex).append("\">").append(methodName + "</a></td>")
                            .append("<td rowspan=\"1\">" + aResult.getName() + "</td></tr>");
                        scenarioIndex++;
                    }

                    scenariosPerClass += resultsCount;
                    methodIndex++;
                }

                // Write the test results for the class
                this.writer.print("<tr class=\"");
                this.writer.print(cssClass);
                this.writer.print("\">");
                this.writer.print("<td rowspan=\"");
                this.writer.print(scenariosPerClass);
                this.writer.print("\">");
                String className = Utils.escapeHtml(classResult.getClassName());
                String ClassName = className.substring(className.lastIndexOf(".")+1);
                this.writer.print(ClassName);//Utils.escapeHtml(classResult.getClassName())
                this.writer.print("</td>");
                this.writer.print(this.buffer);

                classIndex++;

            }
            scenarioCount = scenarioIndex - startingScenarioIndex;

        }
        return scenarioCount;
    }

    /**
     * Writes the details for all test scenarios.
     *
     * @throws UnknownHostException
     */
    protected void writeScenarioDetails() throws UnknownHostException {
        int scenarioIndex = 0;
        for (SuiteResult suiteResult : this.suiteResults) {
            for (TestResult testResult : suiteResult.getTestResults()) {
                this.writer.print("<h2>");
                this.writer.print(Utils.escapeHtml(testResult.getTestName()));
                this.writer.print("</h2>");

                scenarioIndex += writeScenarioDetails(testResult.getFailedConfigurationResults(), scenarioIndex);
                scenarioIndex += writeScenarioDetails(testResult.getFailedTestResults(), scenarioIndex);
                scenarioIndex += writeScenarioDetails(testResult.getSkippedConfigurationResults(), scenarioIndex);
                scenarioIndex += writeScenarioDetails(testResult.getSkippedTestResults(), scenarioIndex);
                scenarioIndex += writeScenarioDetails(testResult.getPassedTestResults(), scenarioIndex);
            }
        }
    }

    /**
     * Writes the scenario details for the results of a given state for a
     * single test.
     *
     * @throws UnknownHostException
     */
    private int writeScenarioDetails(final List<ClassResult> classResults, final int startingScenarioIndex)
        throws UnknownHostException {
        int scenarioIndex = startingScenarioIndex;
        for (ClassResult classResult : classResults) {
            String className = classResult.getClassName();
            for (MethodResult methodResult : classResult.getMethodResults()) {
                List<ITestResult> results = methodResult.getResults();
                assert !results.isEmpty();

                ITestResult mResult = results.iterator().next();
                String label =
                    Utils.escapeHtml(className + "#" + mResult.getMethod().getMethodName() + " ( " + mResult.getName() + " )");
                for (ITestResult result : results) {
                    writeScenario(scenarioIndex, label, result, mResult.getName());
                    scenarioIndex++;
                }
            }
        }

        return scenarioIndex - startingScenarioIndex;
    }

    /**
     * Writes the details for an individual test scenario.
     *
     * @throws UnknownHostException
     */
    private void writeScenario(final int scenarioIndex, final String label, final ITestResult result, final String mResult)
        throws UnknownHostException {
        this.writer.print("<h3 id=\"m");
        this.writer.print(scenarioIndex);
        this.writer.print("\">");
        this.writer.print(label);
        this.writer.print("</h3>");

        this.writer.print("<table class=\"result\">");


        // Write test parameters (if any)
        Object[] parameters = result.getParameters();
        int parameterCount = (parameters == null ? 0 : parameters.length);
        if (parameterCount > 0) {
            this.writer.print("<tr class=\"param\">");
            for (int i = 1; i <= parameterCount; i++) {
                this.writer.print("<th>Parameter #");
                this.writer.print(i);
                this.writer.print("</th>");
            }
            this.writer.print("</tr><tr class=\"param stripe\">");
            for (Object parameter : parameters) {
                this.writer.print("<td>");
                this.writer.print(Utils.escapeHtml(Utils.toString(parameter, Object.class)));
                this.writer.print("</td>");
            }
            this.writer.print("</tr>");
        }

        // Write reporter messages (if any)
        List<String> reporterMessages = Reporter.getOutput(result);
        if (!reporterMessages.isEmpty()) {
            this.writer.print("<tr><th");
            if (parameterCount > 1) {
                this.writer.print(" colspan=\"");
                this.writer.print(parameterCount);
                this.writer.print("\"");
            }
            this.writer.print(">Messages</th></tr>");

            this.writer.print("<tr><td");
            if (parameterCount > 1) {
                this.writer.print(" colspan=\"");
                this.writer.print(parameterCount);
                this.writer.print("\"");
            }
            this.writer.print(">");
            writeReporterMessages(reporterMessages);
            this.writer.print("</td></tr>");
        }

        // Write exception (if any)
        Throwable throwable = result.getThrowable();
        if (throwable != null) {
            this.writer.print("<tr><th");
            if (parameterCount > 1) {
                this.writer.print(" colspan=\"");
                this.writer.print(parameterCount);
                this.writer.print("\"");
            }
            this.writer.print(">");
            this.writer.print((result.getStatus() == ITestResult.SUCCESS ? "Expected Exception" : "Exception"));
            this.writer.print("</th></tr>");

            this.writer.print("<tr><td");
            if (parameterCount > 1) {
                this.writer.print(" colspan=\"");
                this.writer.print(parameterCount);
                this.writer.print("\"");
            }
            this.writer.print(">");
            writeStackTrace(throwable);
            this.writer.print("</td></tr>");
        }

        this.writer.print("</table>");
        this.writer.print("<p class=\"totop\"><a href=\"#summary\">back to summary</a></p>");
    }

    protected void writeReporterMessages(final List<String> reporterMessages) {
        this.writer.print("<div class=\"messages\">");
        Iterator<String> iterator = reporterMessages.iterator();
        assert iterator.hasNext();
        this.writer.print(Utils.escapeHtml(iterator.next()));
        while (iterator.hasNext()) {
            this.writer.print("<br/>");
            this.writer.print(Utils.escapeHtml(iterator.next()));
        }
        this.writer.print("</div>");
    }

    @SuppressWarnings("deprecation")
	protected void writeStackTrace(final Throwable throwable) {
        this.writer.print("<div class=\"stacktrace\">");
        this.writer.print(Utils.stackTrace(throwable, true)[0]);
        this.writer.print("</div>");
    }

    /**
     * Writes a TH element with the specified contents and CSS class names.
     *
     * @param html
     *            the HTML contents
     * @param cssClasses
     *            the space-delimited CSS classes or null if there are no
     *            classes to apply
     */
    protected void writeTableHeader(final String html, final String cssClasses) {
        writeTag("th", html, cssClasses);
    }

    /**
     * Writes a TD element with the specified contents.
     *
     * @param html
     *            the HTML contents
     */
    protected void writeTableData(final String html) {
        writeTableData(html, null);
    }

    /**
     * Writes a TD element with the specified contents and CSS class names.
     *
     * @param html
     *            the HTML contents
     * @param cssClasses
     *            the space-delimited CSS classes or null if there are no
     *            classes to apply
     */
    protected void writeTableData(final String html, final String cssClasses) {
        writeTag("td", html, cssClasses);
    }

    /**
     * Writes an arbitrary HTML element with the specified contents and CSS
     * class names.
     *
     * @param tag
     *            the tag name
     * @param html
     *            the HTML contents
     * @param cssClasses
     *            the space-delimited CSS classes or null if there are no
     *            classes to apply
     */
    protected void writeTag(final String tag, final String html, final String cssClasses) {
        this.writer.print("<");
        this.writer.print(tag);
        if (cssClasses != null) {
            this.writer.print(" class=\"");
            this.writer.print(cssClasses);
            this.writer.print("\"");
        }
        this.writer.print(">");
        this.writer.print(html);
        this.writer.print("</");
        this.writer.print(tag);
        this.writer.print(">");
    }

    /**
     * Groups {@link TestResult}s by suite.
     */
    protected static class SuiteResult {
        private final String suiteName;
        private final List<TestResult> testResults = Lists.newArrayList();

        public SuiteResult(final ISuite suite) {
            this.suiteName = suite.getName();
            for (ISuiteResult suiteResult : suite.getResults().values()) {
                this.testResults.add(new TestResult(suiteResult.getTestContext()));
            }
        }

        public String getSuiteName() {
            return this.suiteName;
        }

        /**
         * @return the test results (possibly empty)
         */
        public List<TestResult> getTestResults() {
            return this.testResults;
        }

    }

    /**
     * Groups {@link ClassResult}s by test, type (configuration or test), and
     * status.
     */
    protected static class TestResult {
        /**
         * Orders test results by class name and then by method name (in
         * lexicographic order).
         */
        protected static final Comparator<ITestResult> RESULT_COMPARATOR = new Comparator<ITestResult>() {
            @Override
            public int compare(final ITestResult o1, final ITestResult o2) {
                int result = o1.getTestClass().getName().compareTo(o2.getTestClass().getName());
                if (result == 0) {
                    result = o1.getMethod().getMethodName().compareTo(o2.getMethod().getMethodName());
                }
                return result;
            }
        };

        private final String testName;
        private final List<ClassResult> failedConfigurationResults;
        private final List<ClassResult> failedTestResults;
        private final List<ClassResult> skippedConfigurationResults;
        private final List<ClassResult> skippedTestResults;
        private final List<ClassResult> passedTestResults;
        private final int failedTestCount;
        private final int skippedTestCount;
        private final int passedTestCount;
        private final long duration;
        private final String includedGroups;
        private final String excludedGroups;

        public TestResult(final ITestContext context) {
            this.testName = context.getName();

            Set<ITestResult> failedConfigurations = context.getFailedConfigurations().getAllResults();
            Set<ITestResult> failedTests = context.getFailedTests().getAllResults();
            Set<ITestResult> skippedConfigurations = context.getSkippedConfigurations().getAllResults();
            Set<ITestResult> skippedTests = context.getSkippedTests().getAllResults();
            Set<ITestResult> passedTests = context.getPassedTests().getAllResults();

            this.failedConfigurationResults = groupResults(failedConfigurations);
            this.failedTestResults = groupResults(failedTests);
            this.skippedConfigurationResults = groupResults(skippedConfigurations);
            this.skippedTestResults = groupResults(skippedTests);
            this.passedTestResults = groupResults(passedTests);

            this.failedTestCount = failedTests.size();
            this.skippedTestCount = skippedTests.size();
            this.passedTestCount = passedTests.size();

            this.duration = context.getEndDate().getTime() - context.getStartDate().getTime();

            this.includedGroups = formatGroups(context.getIncludedGroups());
            this.excludedGroups = formatGroups(context.getExcludedGroups());
        }

        /**
         * Groups test results by method and then by class.
         */
        protected List<ClassResult> groupResults(final Set<ITestResult> results) {
            List<ClassResult> classResults = Lists.newArrayList();
            if (!results.isEmpty()) {
                List<MethodResult> resultsPerClass = Lists.newArrayList();
                List<ITestResult> resultsPerMethod = Lists.newArrayList();

                List<ITestResult> resultsList = Lists.newArrayList(results);
                Collections.sort(resultsList, TestResult.RESULT_COMPARATOR);
                Iterator<ITestResult> resultsIterator = resultsList.iterator();
                assert resultsIterator.hasNext();

                ITestResult result = resultsIterator.next();
                resultsPerMethod.add(result);

                String previousClassName = result.getTestClass().getName();
                String previousMethodName = result.getMethod().getMethodName();
                while (resultsIterator.hasNext()) {
                    result = resultsIterator.next();

                    String className = result.getTestClass().getName();
                    if (!previousClassName.equals(className)) {
                        // Different class implies different method
                        assert !resultsPerMethod.isEmpty();
                        resultsPerClass.add(new MethodResult(resultsPerMethod));
                        resultsPerMethod = Lists.newArrayList();

                        assert !resultsPerClass.isEmpty();
                        classResults.add(new ClassResult(previousClassName, resultsPerClass));
                        resultsPerClass = Lists.newArrayList();

                        previousClassName = className;
                        previousMethodName = result.getMethod().getMethodName();
                    } else {
                        String methodName = result.getMethod().getMethodName();
                        if (!previousMethodName.equals(methodName)) {
                            assert !resultsPerMethod.isEmpty();
                            resultsPerClass.add(new MethodResult(resultsPerMethod));
                            resultsPerMethod = Lists.newArrayList();

                            previousMethodName = methodName;
                        }
                    }
                    resultsPerMethod.add(result);
                }
                assert !resultsPerMethod.isEmpty();
                resultsPerClass.add(new MethodResult(resultsPerMethod));
                assert !resultsPerClass.isEmpty();
                classResults.add(new ClassResult(previousClassName, resultsPerClass));
            }
            return classResults;
        }

        public String getTestName() {
            return this.testName;
        }

        /**
         * @return the results for failed configurations (possibly empty)
         */
        public List<ClassResult> getFailedConfigurationResults() {
            return this.failedConfigurationResults;
        }

        /**
         * @return the results for failed tests (possibly empty)
         */
        public List<ClassResult> getFailedTestResults() {
            return this.failedTestResults;
        }

        /**
         * @return the results for skipped configurations (possibly empty)
         */
        public List<ClassResult> getSkippedConfigurationResults() {
            return this.skippedConfigurationResults;
        }

        /**
         * @return the results for skipped tests (possibly empty)
         */
        public List<ClassResult> getSkippedTestResults() {
            return this.skippedTestResults;
        }

        /**
         * @return the results for passed tests (possibly empty)
         */
        public List<ClassResult> getPassedTestResults() {
            return this.passedTestResults;
        }

        public int getFailedTestCount() {
            return this.failedTestCount;
        }

        public int getSkippedTestCount() {
            return this.skippedTestCount;
        }

        public int getPassedTestCount() {
            return this.passedTestCount;
        }

        public long getDuration() {
            return this.duration;
        }

        public String getIncludedGroups() {
            return this.includedGroups;
        }

        public String getExcludedGroups() {
            return this.excludedGroups;
        }

        /**
         * Formats an array of groups for display.
         */
        protected String formatGroups(final String[] groups) {
            if (groups.length == 0) {
                return "";
            }

            StringBuilder builder = new StringBuilder();
            builder.append(groups[0]);
            for (int i = 1; i < groups.length; i++) {
                builder.append(", ").append(groups[i]);
            }
            return builder.toString();
        }
    }

    /**
     * Groups {@link MethodResult}s by class.
     */
    protected static class ClassResult {
        private final String className;
        private final List<MethodResult> methodResults;

        /**
         * @param className
         *            the class name
         * @param methodResults
         *            the non-null, non-empty {@link MethodResult} list
         */
        public ClassResult(final String className, final List<MethodResult> methodResults) {
            this.className = className;
            this.methodResults = methodResults;
        }

        public String getClassName() {
            return this.className;
        }

        /**
         * @return the non-null, non-empty {@link MethodResult} list
         */
        public List<MethodResult> getMethodResults() {
            return this.methodResults;
        }
    }

    /**
     * Groups test results by method.
     */
    protected static class MethodResult {
        private final List<ITestResult> results;

        /**
         * @param results
         *            the non-null, non-empty result list
         */
        public MethodResult(final List<ITestResult> results) {
            this.results = results;
        }

        /**
         * @return the non-null, non-empty result list
         */
        public List<ITestResult> getResults() {
            return this.results;
        }


    }

    /*
     * Methods to improve time display on report
     */
    protected String getSysInfoAsString() throws UnknownHostException {

        try {

            String username = System.getProperty("user.name");
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            String hostname = addr.getHostName();
            String SysInfo = username + " @ " + hostname;
            return SysInfo;
        } catch (Exception e) {
            throw (e);
        }
    }

    protected String getDateAsString() {
        Date date = new Date();
        CustomReport.sdfdate.setTimeZone(TimeZone.getTimeZone(CustomReport.timeZone));
        return CustomReport.sdfdate.format(date);
    }

    protected String parseUnixTimeToTimeOfDay(final long unixSeconds) {
        Date date = new Date(unixSeconds);
        CustomReport.sdftime.setTimeZone(TimeZone.getTimeZone(CustomReport.timeZone));
        return CustomReport.sdftime.format(date);
    }

    protected double millisecondsToSeconds(final long ms) {
        return new BigDecimal(ms / 1000.00).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
    
    public static String ReadValueFromPropertiesFile(String strKey, String fileName) throws Exception
	{
		try{
			String strPropertiesFilePath = OR.getConfig("Path_ExecutionTime")+"\\"+fileName;				 
			Properties propertiesFile = new Properties();
			propertiesFile.load(new FileInputStream(strPropertiesFilePath));            
			Enumeration e = propertiesFile.propertyNames();
			String lkey;	
			while (e.hasMoreElements())			
			{
				lkey = (String)e.nextElement();				
				if(lkey.trim().equalsIgnoreCase(strKey.trim())){					
					return propertiesFile.getProperty(lkey);
				}				        
			}
			return "";
		}
		catch(Exception e)
		{
			System.out.println("Exception occured :: "+ e.getMessage());
			return "";
		}
	}

}
