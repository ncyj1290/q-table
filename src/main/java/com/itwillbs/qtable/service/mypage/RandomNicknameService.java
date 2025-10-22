package com.itwillbs.qtable.service.mypage;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RandomNicknameService {

	private final RandomNickname randomNickname;

	public void example() {
		String nickname = randomNickname.generate();
	}
	
	

}
