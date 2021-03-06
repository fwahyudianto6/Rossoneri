package com.fwahyudianto.rossoneri;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.fwahyudianto.rossoneri.controllers.PlayerGridAdapter;
import com.fwahyudianto.rossoneri.controllers.PlayerItemClickSupport;
import com.fwahyudianto.rossoneri.controllers.PlayerListAdapter;
import com.fwahyudianto.rossoneri.models.Player;
import com.fwahyudianto.rossoneri.models.PlayerData;

import java.util.ArrayList;

/**
 * This software, all associated documentation, and all copies are CONFIDENTIAL INFORMATION of Kalpawreska Teknologi Indonesia
 * http://www.fwahyudianto.id
 * ® Wahyudianto, Fajar
 * Email 	: fwahyudi06@gmail.com
 */

public class HomeActivity extends AppCompatActivity {
    final String m_strStateTitle = "state_title";
    final String m_strStateList = "state_list";
    final String m_strStateMode = "state_mode";
    int iMode;
    private long m_lngBackPressed;
    private Toast m_oToast;
    private RecyclerView oRecyclerView;
    private ArrayList<Player> alData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        oRecyclerView = findViewById(R.id.rv_main);
        oRecyclerView.setHasFixedSize(true);

        alData = new ArrayList<>();

        if (savedInstanceState == null) {
            setActionBarTitle("Hall of Fame - List View");
            alData.addAll(PlayerData.List());
            RecyclerList();
            iMode = R.id.menu_list;
        } else {
            String strTitle = savedInstanceState.getString(m_strStateTitle);
            ArrayList<Player> oPlayer = savedInstanceState.getParcelableArrayList(m_strStateList);
            int iStateMode = savedInstanceState.getInt(m_strStateMode);

            setActionBarTitle(strTitle);
            alData.addAll(oPlayer);
            setMode(iStateMode);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle oBundle) {
        super.onSaveInstanceState(oBundle);
        oBundle.putString(m_strStateTitle, getSupportActionBar().getTitle().toString());
        oBundle.putParcelableArrayList(m_strStateList, alData);
        oBundle.putInt(m_strStateMode, iMode);
    }

    @Override
    public void onBackPressed() {
        if (m_lngBackPressed + 2000 > System.currentTimeMillis()) {
            m_oToast.cancel();
            super.onBackPressed();
            return;
        } else {
            m_oToast = Toast.makeText(getBaseContext(), "Press back again to Exit!", Toast.LENGTH_SHORT);
            m_oToast.show();
        }

        m_lngBackPressed = System.currentTimeMillis();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu p_oMenu) {
        getMenuInflater().inflate(R.menu.menu_home, p_oMenu);

        return super.onCreateOptionsMenu(p_oMenu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem p_oMenuItem) {
        setMode(p_oMenuItem.getItemId());

        return super.onOptionsItemSelected(p_oMenuItem);
    }

    //  Method
    private void RecyclerList(){
        oRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        PlayerListAdapter oPlayerListAdapter = new PlayerListAdapter(this);
        oPlayerListAdapter.setPlayer(alData);

        oRecyclerView.setAdapter(oPlayerListAdapter);

        //  Added Toast on Item
        PlayerItemClickSupport.addTo(oRecyclerView).setOnItemClickListener(new PlayerItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView p_oRecyclerView, int p_iPosition, View p_oView) {
                selectedItemDetail(alData.get(p_iPosition));
            }
        });
    }

    private void RecyclerGrid(){
        oRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        PlayerGridAdapter oPlayerGridAdapter = new PlayerGridAdapter(this);
        oPlayerGridAdapter.setPlayer(alData);

        oRecyclerView.setAdapter(oPlayerGridAdapter);

        //  Added Toast on Item
        PlayerItemClickSupport.addTo(oRecyclerView).setOnItemClickListener(new PlayerItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView p_oRecyclerView, int p_iPosition, View p_oView) {
                selectedItem(alData.get(p_iPosition));
            }
        });
    }

    private void setActionBarTitle(String p_strTitle) {
        getSupportActionBar().setTitle(p_strTitle);
    }

    private void setMode(int p_iMode) {
        String strTitle = null;
        switch (p_iMode) {
            case R.id.menu_list:
                strTitle = "Hall of Fame - List View";
                RecyclerList();
                break;
            case R.id.menu_grid:
                strTitle = "Hall of Fame - Grid View";
                RecyclerGrid();
                break;
        }

        iMode = p_iMode;
        setActionBarTitle(strTitle);
    }

    private void selectedItem(Player p_oPlayer){
        Toast.makeText(this, "You choose : " + p_oPlayer.getNickName(), Toast.LENGTH_SHORT).show();
    }

    private void selectedItemDetail(Player p_oPlayerDetail) {
        Intent oPlayerDetail = new Intent(HomeActivity.this, PlayerDetailActivity.class);
        oPlayerDetail.putExtra(PlayerDetailActivity.m_strStateList, p_oPlayerDetail);
        startActivity(oPlayerDetail);
    }
}