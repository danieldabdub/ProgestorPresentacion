import java.util.Vector;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

public class QualificationData{
    
    // Poner aqui los nombres que finalmente se usen.
    
    String qualificationId;
	String qualification;
	String description;
    
    // Primer Constructor: - getQualificationList
    //                     - getQualification
    
    QualificationData (String qualificationId, String qualification, String description){
        this.qualificationId = qualificationId;
		this.qualification = qualification;
		this.description = description;
    }
    
    public static Vector<QualificationData> getQualificationList(Connection connection){
        
        Vector<QualificationData> vec = new Vector<QualificationData>();
        
        String sql = "SELECT qualificationId, qualification, description FROM Qualifications";
        System.out.println("getQualificationList: " + sql);
        
        try {
            Statement statement=connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            
            while(result.next()) {

                
                QualificationData qualification = new QualificationData(
                    result.getString("qualificationId"),
                    result.getString("qualification"),
                    result.getString("description")
                );
                
                vec.addElement(qualification);
            }
            
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println("Error in getQualificationList: " + sql + " Exception: " + e);
        }
        return vec;
    }
    
    public static QualificationData getQualification(Connection connection, String qualificationId){
        
        String sql = "SELECT qualificationId, qualification, description FROM Qualifications WHERE qualificationId=?";
        System.out.println("getQualification: " + sql);
        
        QualificationData qualification = null;
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, qualificationId);
            
            ResultSet result = pstmt.executeQuery();
            
            if(result.next()){
                qualification = new QualificationData(
                    result.getString("qualificationId"),
                    result.getString("qualification"),
                    result.getString("description")
                );  
            }
            result.close();
            pstmt.close();
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println("Error in getQualification: " + sql + " Exception: " + e);
        }
        
        return qualification;
        
    }
    
    public static int updateQualification(Connection connection, QualificationData qualification){

        String sql="UPDATE Qualifications SET description = ? WHERE qualificationId = ?";
        
        System.out.println("updateQualification: " + sql);
        
        int n = 0;
        
        try {
            PreparedStatement stmtUpdate= connection.prepareStatement(sql);
            
            stmtUpdate.setString(1,qualification.description);
            stmtUpdate.setString(2,qualification.qualificationId);
            
            n = stmtUpdate.executeUpdate();
            stmtUpdate.close();
        } catch(SQLException e){
            e.printStackTrace();
            System.out.println("Error in updateQualification: " + sql + " Exception: " + e);   
        }
        return n;
    }
        
}

class EmployeeQualificationData {
    String employeeId;
    String    qualificationId;
    String    qualification;
    Date qualificationDate;

    
    EmployeeQualificationData (String employeeId, String qualificationId, String qualification, Date qualificationDate) {
        this.employeeId    = employeeId;
        this.qualificationId   = qualificationId;
        this.qualification = qualification;
        this.qualificationDate = qualificationDate;
    }
        
    public static Vector<EmployeeQualificationData> getEmployeeQualifications(Connection connection, String employeeId) {
        Vector<EmployeeQualificationData> vec = new Vector<EmployeeQualificationData>();
        String sql = "Select Qualifications.qualificationId as qualificationId, qualification, qualificationDate FROM Qualifications, EmployeeQualifications";
        sql += " WHERE Qualifications.qualificationId = EmployeeQualifications.qualificationId AND employeeId = ?";
        System.out.println("getEmployeeQualifications: " + sql);
        try {
            PreparedStatement statement= connection.prepareStatement(sql);
            statement.setString(1, employeeId);
            ResultSet result = statement.executeQuery();
            while(result.next()) {
                EmployeeQualificationData employeeQualifications = new EmployeeQualificationData(
                    employeeId,
                    result.getString("qualificationId"),
                    result.getString("qualification"),
                    result.getDate("qualificationDate")
                );
                vec.addElement(employeeQualifications);
            }
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println("Error in getEmployeeQualifications: " + sql + " Exception: " + e);
        }
        return vec;
    }
}
