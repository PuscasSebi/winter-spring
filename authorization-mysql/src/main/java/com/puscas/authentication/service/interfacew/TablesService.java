package com.puscas.authentication.service.interfacew;

import com.puscas.authentication.model.UserPlaceScheduler;
import org.springframework.jdbc.core.JdbcTemplate;
import org.thymeleaf.util.StringUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class TablesService {

    private JdbcTemplate jdbcTemplate;

    private String currentTableName;

    private String scheduleTableName = "userPlaceScheduler";

    private String createTablePrefix ="create table ";
    private String createTableSuffix ="( SCHEDULE_ID int(20)," +
            "USER_ID int(20)," +
            "PLACE_ID varchar(20)," +
            "TIME_CREATE varchar(15)," +
            "FOREIGN KEY (USER_ID) REFERENCES Student(id)," +
            "FOREIGN KEY (PLACE_ID) REFERENCES place(id)," +
            "PRIMARY KEY(SCHEDULE_ID)" +
            ");";

    private String insertQueryPrefix = "insert into";
    private String insertQuerySuffix = "(SCHEDULE_ID,USER_ID,PLACE_ID, TIME_CREATED) values(?,?,?,?)";

    /**
     *   @Id
     *     @GeneratedValue(strategy = GenerationType.AUTO)
     *     private Integer id;
     *
     *     @Column(name = "creator")
     *     public String placeCreator;
     *
     *     @Column(name = "name")
     *     public String placeName;
     *
     *     @Column(name = "name")
     *     public String descriptionName;
     *
     *     @Column(name = "image")
     *     public String placeImage;
     *     private String idOfSchedule; //year+month+day+hour
     *     private Long userIdentifier;
     *     private String placeIdentifier;
     *     private String timeCreated;
     *
     * @param jdbcTemplate
     */

    public TablesService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String getTableNameForCurrentMonth(){
        LocalDate localDate = Instant.now().atZone(ZoneId.systemDefault()).toLocalDate();

        int month = localDate.getMonthValue();
        int year = localDate.getYear();

        return scheduleTableName + month+ year;
    }

    public String currentTableName(){
        String tableNameForCurrentMonth = getTableNameForCurrentMonth();
        if(StringUtils.isEmpty(currentTableName) || !currentTableName.equals(tableNameForCurrentMonth)){
            jdbcTemplate.execute(createTablePrefix + tableNameForCurrentMonth + createTableSuffix);
            currentTableName = tableNameForCurrentMonth;
        }
        return tableNameForCurrentMonth;
    }

    public void createAScheduleForAPlace(UserPlaceScheduler userPlaceScheduler){
        Object[] args = new Object[] {userPlaceScheduler.getIdOfSchedule(), userPlaceScheduler.getUserIdentifier(),
                userPlaceScheduler.getPlaceIdentifier(), userPlaceScheduler.getTimeCreated()};
        String currentTableName = currentTableName();
        String queryToInsert = insertQueryPrefix + currentTableName + insertQuerySuffix;
        int out = jdbcTemplate.update(queryToInsert, args);

        if(out !=0){
            System.out.println("UserPlaceScheduler saved with id="+userPlaceScheduler.getIdOfSchedule());
        }else System.out.println("UserPlaceScheduler save failed with id="+userPlaceScheduler.getIdOfSchedule());

    }


}
