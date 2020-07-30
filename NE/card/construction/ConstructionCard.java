package NE.card.construction;

import NE.card.Card;

public abstract class ConstructionCard extends Card {

    protected int minHands;
    protected int amountsToBuild;

    public ConstructionCard() {

    }
    // template method pattern を使う場合
    // 共通している処理は、最初と最後の処理
    // 最初のやつは、立てられるカードがない時等のreturn false
    // 最後のやつはコスト分のカードを捨てる処理
}