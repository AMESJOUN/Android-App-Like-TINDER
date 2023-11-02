package com.example.tinder.Matches;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tinder.R;

class MatchesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView mMessage;
    public LinearLayout mContainer;

  //  public TextView mMatchId,mMatchName;
   // public ImageView mMatchImage;
    public MatchesViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
    //    mMatchId = (TextView) itemView.findViewById(R.id.Matchid);
     //   mMatchName = (TextView) itemView.findViewById(R.id.MatchName);
      //  mMatchImage = (ImageView) itemView.findViewById(R.id.MatchImage);

        mMessage = itemView.findViewById(R.id.message);
        mContainer = itemView.findViewById(R.id.container);

    }


    @Override
    public void onClick(View view) {

     /*  Intent intent = new Intent(view.getContext(), ChatActivity.class);
        Bundle b = new Bundle();
        b.putString("machdId",mMatchId.getText().toString());
        intent.putExtras(b);
        view.getContext().startActivity(intent);*/


    }
}
