package com.example.xmeng.imagesearch.Activities;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.View;
import android.widget.AdapterView;

import com.etsy.android.grid.StaggeredGridView;
import com.example.xmeng.imagesearch.Adapter.GridViewAdapter;
import com.example.xmeng.imagesearch.Fragments.FilterDialogFragment;
import com.example.xmeng.imagesearch.Models.QueryImage;
import com.example.xmeng.imagesearch.R;
import com.example.xmeng.imagesearch.Util.EndlessScrollListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.logging.Filter;

import cz.msebera.android.httpclient.Header;

public class ImageSearchActivity extends ActionBarActivity implements FilterDialogFragment.FilterDialogListener {

    String baseUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=";
    public ArrayList<QueryImage> images;
    public GridViewAdapter gvAdapter;
    private String size = "";
    private String color = "";
    private String type = "";
    private String site = "";
    private int rsz = 8;
    private int current = 0;
    private String searchQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search);
        images = new ArrayList<QueryImage>();
        gvAdapter = new GridViewAdapter(this, images);
        StaggeredGridView sgView = (StaggeredGridView) findViewById(R.id.grid_view);
        sgView.setAdapter(gvAdapter);
        sgView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ImageSearchActivity.this, ImageDisplayActivity.class);
                i.putExtra("url", images.get(position).url);
                startActivity(i);
            }
        });
        sgView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                fetchImages(searchQuery);
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_image_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                images.clear();
                current = 0;
                searchQuery = query;
                fetchImages(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        MenuItem settingsItem = menu.findItem(R.id.action_settings);
        settingsItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Bundle args = new Bundle();
                args.putString("color",color);
                args.putString("type",type);
                args.putString("size",size);
                args.putString("site",site);
                FragmentManager fm = getFragmentManager();
                DialogFragment filterDialog = new FilterDialogFragment();
                filterDialog.setArguments(args);
                filterDialog.show(fm, "filer_dialog");
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void fetchImages(String query) {
        String url = baseUrl + query + "&imgcolor="+this.color + "&imgsz="+this.size+"&imgtype="+this.type + "&as_sitesearch=" + this.site + "&rzs=8" + "&start="+this.current;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("debug", response.toString());
                JSONArray imageList = null;
                try{
                    imageList = response.getJSONObject("responseData").getJSONArray("results");
                    for(int i = 0; i < imageList.length(); i ++){
                        JSONObject image = imageList.getJSONObject(i);
                        QueryImage qi = new QueryImage();
                        qi.content = image.getString("title");
                        qi.url = image.getString("url");
                        qi.tbURL = image.getString("tbUrl");
                        images.add(qi);
                    }
                    gvAdapter.notifyDataSetChanged();
                    current+= 8;

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFinishEditing(String size, String color, String type, String site) {
        this.size = size;
        this.color = color;
        this.type = type;
        this.site = site;
    }
}
