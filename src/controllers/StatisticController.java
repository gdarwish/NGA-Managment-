package controllers;

import Const.Const;
import Database.CSP.CSP;
import Database.CSP.CSPDAO;
import Database.Project.Project;
import Database.Project.ProjectDAO;
import Database.Project.sort.ProjectSortStartDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.*;

/**
 * @author Ghaith Darwish
 * Creating the CategoriesContoller that handles all Categories Tables
 */
public class StatisticController implements Initializable {

    @FXML
    private PieChart piechart;
    @FXML
    private LineChart<String, Number> lineChart;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private HBox datePickerHbox;
    ProjectDAO projectDAO;
    CSPDAO cspdao;
    ObservableList<PieChart.Data> pieChartData;
    HashMap<String, Integer> statisticList;
    ArrayList<Project> openProject;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        projectDAO = ProjectDAO.getInstance();
    }

    @FXML
    private void statusButtonEvent(ActionEvent event) {
        createPieChart(Const.TABLE_STATUS, "Status");
    }

    private String getMonthName(int month) {
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return months[month];
    }

    @FXML
    private void categoriesButtonEvent(ActionEvent event) {
        createPieChart(Const.TABLE_CATEGORY, "Category");
    }

    @FXML
    private void priorityButtonEvent(ActionEvent event) {
        createPieChart(Const.TABLE_PRIORITY, "Priority");
    }


    public void createPieChart(String table, String title) {
        datePickerHbox.setStyle("visibility: false");
        cspdao = new CSPDAO(table);
        int totalProjects = projectDAO.getFilteredProjects(Const.OPEN).size();
        statisticList = new HashMap<String, Integer>();
        openProject = projectDAO.getFilteredProjects(Const.OPEN);
        pieChartData = FXCollections.observableArrayList();
        for (CSP csp : cspdao.getAll()) {
            statisticList.put(csp.getName(), projectDAO.getProjectsCountByCSP(openProject, csp));
        }
        for (Map.Entry<String, Integer> data : statisticList.entrySet()) {
            double parentage = 100.0 * data.getValue() / totalProjects;
            pieChartData.add(new PieChart.Data(data.getKey() + " %" + String.format("%.2f", parentage), data.getValue()));
        }
        piechart.setTitle(title + " Statistic");
        piechart.setData(pieChartData);

        lineChart.setStyle("visibility: false");
        piechart.setStyle("visibility: true");
    }

    public void dateButtonEvent(ActionEvent event) {
        createLineChart();
    }

    public void dateChanged(ActionEvent actionEvent) {
        createLineChart();
    }

    private void createLineChart() {
        datePickerHbox.setStyle("visibility: true");
        lineChart.setStyle("visibility: true");
        piechart.setStyle("visibility: false");
        statisticList = new LinkedHashMap<>();
        XYChart.Series<String, Number> openProjectsSeries = new XYChart.Series();
        openProjectsSeries.setName("Open Projects");

        ArrayList<Project> openProjects = projectDAO.getFilteredProjects(Const.OPEN);
        ArrayList<Project> openProjectsWithRange = projectDAO.getProjectsByDate(openProjects, startDatePicker.getValue(), endDatePicker.getValue());
        openProjectsWithRange.sort(new ProjectSortStartDate());
        for (Project project : openProjectsWithRange) {
            int count = projectDAO.getProjectsCountByDate(openProjectsWithRange, project.getStartDate());
            openProjectsSeries.getData().add(new XYChart.Data(getMonthName(project.getStartDate().getMonth()), count));
            statisticList.put(getMonthName(project.getStartDate().getMonth()), count);
        }
        lineChart.getData().clear();
        lineChart.getData().addAll(openProjectsSeries);
    }
}