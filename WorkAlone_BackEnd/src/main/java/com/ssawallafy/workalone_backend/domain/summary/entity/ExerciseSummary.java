package com.ssawallafy.workalone_backend.domain.summary.entity;

import java.time.LocalTime;

import com.ssawallafy.workalone_backend.domain.exercise.entity.Exercise;
import com.ssawallafy.workalone_backend.domain.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ExerciseSummary {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	private LocalTime date;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "exercise_id")
	private Exercise exercise;

	private int kcal;

	private LocalTime time;

}
