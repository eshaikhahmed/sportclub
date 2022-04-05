package com.fun.sportclub.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="batch")
public class BatchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long Id;

    @Column(name = "batch_name")
    private String batchName;

    @Column(name = "batch_time")
    private String batchTime;

    @Column(name = "batch_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date batchDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sport_id", referencedColumnName = "id")
    private SportEntity sport;

    @OneToMany(mappedBy="batch")
    private Set<BatchMemberEntity> memberList;

    @Column(name = "created_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date createdDate;

}