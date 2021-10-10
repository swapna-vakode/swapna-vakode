package com.cg.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.*;

import com.cg.entity.Insurance;
import com.cg.insuranceDao.InsuranceDao;
import com.cg.insuranceDao.InsuranceDaoImpl;

public class InsuranceServiceImpl implements InsuranceService {

	private InsuranceDao dao;

	public InsuranceServiceImpl() {
		dao = new InsuranceDaoImpl();
	}

	@Override
	public int calculateInsurance(Insurance insurance) {
		LocalDate today = LocalDate.now();
		int years = today.getYear() - insurance.getPurchaseYear();

		String insuranceType = insurance.getInsuranceType();
		double onRoadPrice = insurance.getOnRoadPrice();
		double value = 1;
		if (insuranceType.equals("Third Party")) {
			value = 0.02;
		} else if (insuranceType.equals("Comprehensive")) {
			value = 0.03;
		}

		double depreciation = years * (onRoadPrice * 0.05);
		double amount = (onRoadPrice - depreciation) * value;
		insurance.setInsuranceAmount(amount);

		LocalDate exp = today.plusYears(1);
		insurance.setExpiryDate(new Date(exp.toEpochDay()));

		return dao.saveInsurance(insurance);
	}

	@Override
	public Insurance fetchInsuranceById(int id) {

		return dao.getInsuranceById(id);
	}

	@Override
	public Insurance fetchInsuranceByRegistrationNo(String regNo) {
		return dao.getInsuranceByRegistrationNo(regNo);
	}

	@Override
	public List<Insurance> getList() {
		return dao.getInsurances();
	}
}
