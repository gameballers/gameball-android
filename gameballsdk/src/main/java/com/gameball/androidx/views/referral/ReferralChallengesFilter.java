package com.gameball.androidx.views.referral;

import com.gameball.androidx.model.response.Game;

import io.reactivex.functions.Predicate;

public class ReferralChallengesFilter implements Predicate<Game>
{
    @Override
    public boolean test(Game game) throws Exception
    {
        return game.isReferral();
    }
}
