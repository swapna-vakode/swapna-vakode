package com.cg.insuranceDao;

import java.util.ArrayList;
import java.util.List;

import com.cg.entity.Insurance;

public class InsuranceDaoImpl implements InsuranceDao {
	public List<Insurance> insuranceses;
	Insurance insurance;

	public InsuranceDaoImpl() {
		insuranceses = new ArrayList<Insurance>();

	}

	@Override
	public int saveInsurance(Insurance insurance) {
		insuranceses.add(insurance);

		System.out.println("Your insurance service has been created successfully for your bike.\nYour Insurance id is: "
				+ insurance.getInsuranceId());
		return 0;
	}

	@Override
	public Insurance getInsuranceById(int id) {

		int flag = 0;
		try {

			for (Insurance x : insuranceses) {

				if (x.getInsuranceId() == id) {
					insurance = x;
					flag = 1;
					break;
				}

			}
			if (flag == 1) {
				System.out.println("Insurance found");
				return insurance;
			} else {
				System.out.println("Insurance not found");
				return null;
			}

		} catch (Exception e) {
			System.out.println("Error occured.");
			return null;
		}

	}

	@Override
	public Insurance getInsuranceByRegistrationNo(String regNo) {

		int flag = 0;
		try {
			for (Insurance x : insuranceses) {
				if (x.getRegistrationNo().equals(regNo)) {
					insurance = x;
					flag = 1;
					break;
				}

			}
			if (flag == 1) {
				System.out.println("Insurance found.");
				return insurance;
			} else {
				System.out.println("Insurance not found.");
				return null;
			}

		} catch (Exception e) {
			System.out.println("Error occured");
			return null;
		}

	}

	@Override
	public List<Insurance> getInsurances() {
		return insuranceses;
	}

}
