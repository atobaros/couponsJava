package phase3.core.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import phase3.core.entities.Company;
import phase3.core.entities.Coupon;
import phase3.core.entities.Coupon.Category;
import phase3.core.repositories.CouponsExceptions;
import phase3.core.service.CompanyImpl;
import phase3.core.sessions.Session;
import phase3.core.sessions.SessionContext;

@CrossOrigin
@RestController
@RequestMapping("/company")
public class CompanyController extends ClientController {

	@Autowired
	private CompanyImpl companyImpl;

	@Autowired
	private SessionContext sessionContext;


	@Override
	public ResponseEntity<?> login(String email, String password) {
		try {
			companyImpl.login(email, password);
			
			return ResponseEntity.status(HttpStatus.OK).body("company " + companyImpl.getCompanyDetails().getName() + " has logged in" );
		} catch (CouponsExceptions e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}

	}

	@PostMapping("/add-coupon")
	public ResponseEntity<?> addCoupon(@RequestHeader String token, @RequestBody Coupon coupon) {

		try {
			companyImpl.addCoupon(coupon);
			return ResponseEntity.ok(coupon);
		} catch (CouponsExceptions e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}

	}

	@PutMapping("/update-coupon")
	public ResponseEntity<?> updateCoupon(@RequestHeader String token, @RequestBody Coupon coupon) {

		try {
			companyImpl.updateCoupon(coupon);
			return ResponseEntity.ok(coupon);
		} catch (CouponsExceptions e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());

		}

	}

	@DeleteMapping("/delete-coupon/{couponId}")
	public ResponseEntity<?> deleteCoupon(@RequestHeader String token,@PathVariable int couponId) {

		try {

			companyImpl.deleteCoupon(couponId);
			return ResponseEntity.ok(couponId);
		} catch (CouponsExceptions e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Coupon Id doesnt exists");
		}

	}

	@GetMapping("/get-all-company-coupons")
	public ResponseEntity<?> getCompaniesCoupons(@RequestHeader String token) {

		List<Coupon> list;
		try {
			list = companyImpl.getCompaniesCoupons();
			return ResponseEntity.ok(list);
		} catch (CouponsExceptions e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}

	}

	@GetMapping("/get-company-coupons-by-category/{category}")
	public ResponseEntity<?> getCompaniesCoupons(@RequestHeader String token,@PathVariable Category category) {

		List<Coupon> list;
		try {
			list = companyImpl.getCompaniesCoupons(category);
			return ResponseEntity.ok(list);
		} catch (CouponsExceptions e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}

	}

	@GetMapping("/get-compay-coupons-max-price/{maxPrice}")
	public ResponseEntity<?> getCompaniesCoupons(@RequestHeader String token,@PathVariable double maxPrice) {

		List<Coupon> list;
		try {
			list = companyImpl.getCompaniesCoupons(maxPrice);
			return ResponseEntity.ok(list);
		} catch (CouponsExceptions e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());

		}

	}

	@GetMapping("/get-compay-details")
	public ResponseEntity<?> getCompanyDetails(@RequestHeader String token) {
		Company com;
		try {
			com = companyImpl.getCompanyDetails();
			return ResponseEntity.ok(com);
		} catch (CouponsExceptions e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}

	}
	@PostMapping(path = "/logout/{token}")
public ResponseEntity<?> logout(@PathVariable String token) {
		
		Session session = this.sessionContext.getSession(token);
		if(session!=null) {
			this.sessionContext.sessionsMap.remove(token);
			System.out.println("token remove " + token);
			return ResponseEntity.ok("you have logged out successfuly");
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Sorry, there is a problem with your logout");

	}

}
