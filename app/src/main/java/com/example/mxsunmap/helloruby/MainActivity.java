package com.example.mxsunmap.helloruby;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.BoolRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.CheckoutResult;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.lib.RepositoryCache;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context mContext = getApplicationContext();
        File path = mContext.getExternalFilesDir("repo");
        File mDir = new File(path, "ed");
        if (!mDir.exists()) {
            mDir.mkdir();
        }
        String remote_ = "http://192.168.5.7/yangyu/LLDataConverter.git";
        remote_ = "http://192.168.5.7/yangyu/eEyeData.git";
        new updateMapTask(mDir, remote_).execute();
        setContentView(R.layout.activity_main);
    }

    public static boolean cloneRepo(File f, String remote_) {
        CloneCommand cloneCommand = Git.cloneRepository()
                //          .setURI("https://github.com/grystudy/test.git").setCloneAllBranches(true)
                .setURI(remote_).setCloneAllBranches(true)
//                .setProgressMonitor(new RepoCloneMonitor())
//                .setTransportConfigCallback(new SgitTransportCallback())
                .setDirectory(f)
                .setCloneSubmodules(false);
        try {
            Git newgitdegub = cloneCommand.call();
        }

        catch (OutOfMemoryError e) {
            return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static Git getGit(File repoFile) {
        try {
            Git mGit = Git.open(repoFile);
            return mGit;
        } catch (RepositoryNotFoundException e) {
            return null;
        } catch (Throwable e) {
            return null;
        }
    }

    public static boolean status(Git git) {
        if (git == null) {
            return false;
        }
        try {
            org.eclipse.jgit.api.Status status = git.status().call();
            if (status.isClean()) return true;
        } catch (NoWorkTreeException e) {
            return false;
        } catch (GitAPIException e) {
            return false;
//        }
//        catch (StopTaskException e) {
//            return false;
        } catch (Throwable e) {
            return false;
        }
        return false;
    }

    class updateMapTask extends AsyncTask<String, String, Boolean> {
        private File f;
        private String mRemote;

        public updateMapTask(File ff, String remote_) {
            this.f = ff;
            this.mRemote = remote_;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            Git git = MainActivity.getGit(f);
            if (git == null || MainActivity.status(git) == false) {
                MainActivity.deleteFile(f);
                MainActivity.cloneRepo(f, mRemote);
            } else {
                PullCommand pullCommand = git.pull()
                        .setRemote("origin");
                try {
                    pullCommand.call();
                } catch (Exception e) {
                    MainActivity.deleteFile(f);
                    MainActivity.cloneRepo(f, mRemote);
                    return false;
                }
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            Log.i("mylog", "请求结果为-->" + "ok");
        }
    }

    public static void deleteFile(File file) {
        if (file.exists() == false) return;
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }
            for (int i = 0; i < childFiles.length; i++) {
                deleteFile(childFiles[i]);
            }
            file.delete();
        }
    }
}
