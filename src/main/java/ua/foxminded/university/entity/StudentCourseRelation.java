package ua.foxminded.university.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="students_courses", schema = "schedule")
public class StudentCourseRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "relation_id")
    private String relationId;
    
    @Column(name = "user_id")
    private String userId;
    
    @Column(name = "course_id")
    private String courseId;
}
